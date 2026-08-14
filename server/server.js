/* Pipilika — backend server.
   Serves the store (index.html) and sends REAL order emails via Nodemailer.
   Run:  cd server && npm run start   (or: node server.js)
   Then open http://localhost:3000
*/
const path = require("path");
const express = require("express");
require("dotenv").config();

const { sendOrderEmails } = require("./mail");

const app = express();
app.use(express.json());

/* Permissive CORS so the store can call this API from any host (or file://) during testing. */
app.use(function (req, res, next) {
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
  res.setHeader("Access-Control-Allow-Headers", "Content-Type");
  if (req.method === "OPTIONS") return res.sendStatus(204);
  next();
});

const PORT = Number(process.env.PORT || 3000);
const siteDir = path.resolve(__dirname, ".."); /* the folder that holds index.html */

function smtpReady() {
  return !!(process.env.SMTP_HOST && process.env.SMTP_USER && process.env.SMTP_PASS);
}

/* ---------- health check ---------- */
app.get("/api/health", function (req, res) {
  res.json({ ok: true, smtp: smtpReady(), site: "index.html" });
});

/* ---------- place an order (email sending happens here) ---------- */
app.post("/api/orders", async function (req, res) {
  const o = req.body || {};
  const id = String(o.id || "").trim();
  const email = String(o.email || "").trim();

  if (!id || !email || !/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
    return res.status(400).json({ ok: false, error: "Missing or invalid order id / email" });
  }

  if (!smtpReady()) {
    console.warn("[Pipilika] SMTP not configured (server/.env) — order " + id + " accepted but NO email was sent.");
    return res.json({ ok: true, emails: [], warning: "SMTP not configured in server/.env" });
  }

  try {
    const results = await sendOrderEmails({
      id: id,
      name: o.name || "",
      email: email,
      city: o.city || "",
      country: o.country || "",
      days: Number(o.days) || 7,
      date: o.date || new Date().toISOString(),
      addr: o.addr || "",
      items: Array.isArray(o.items) ? o.items : [],
      total: Number(o.total) || 0
    });
    console.log("[Pipilika] Emails sent for order " + id + " -> " + results.join(", "));
    res.json({ ok: true, emails: results });
  } catch (err) {
    console.error("[Pipilika] Email sending failed for order " + id + ":", err.message);
    res.status(500).json({ ok: false, error: "Email sending failed: " + (err && err.message) });
  }
});

/* ---------- serve the single-file store ---------- */
app.use(express.static(siteDir));

app.listen(PORT, function () {
  console.log("");
  console.log("  Pipilika store + email backend running on  http://localhost:" + PORT);
  console.log(smtpReady()
    ? "  SMTP: configured  ✔  Emails will be sent on every order."
    : "  SMTP: NOT configured  —  fill server/.env (see .env.example), then restart.");
  console.log("");
});