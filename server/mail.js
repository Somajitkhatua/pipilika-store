/* Pipilika — Nodemailer (real email sending) */
const nodemailer = require("nodemailer");
require("dotenv").config();

const transport = nodemailer.createTransport({
  host: process.env.SMTP_HOST,
  port: Number(process.env.SMTP_PORT || 587),
  secure: process.env.SMTP_SECURE === "true",
  auth: {
    user: process.env.SMTP_USER,
    pass: process.env.SMTP_PASS
  }
});

function fmt(n) { return "₹" + Number(n || 0).toLocaleString("en-IN"); }
function esc(s) {
  return String(s == null ? "" : s)
    .replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;").replace(/'/g, "&#39;");
}
function datePlus(iso, days) {
  const d = new Date(iso);
  d.setDate(d.getDate() + days);
  return d.toLocaleDateString("en-IN", { weekday: "long", day: "numeric", month: "long", year: "numeric" });
}
function itemsList(order) {
  if (!order.items || !order.items.length) return "";
  const rows = order.items.map(function (it) {
    return "<tr><td style='padding:8px 0'>" + esc(it.name) +
      " <span style='color:#6b5540'>× " + (it.qty || 1) + "</span></td>" +
      "<td style='padding:8px 0;text-align:right'><b>" + fmt(it.price * (it.qty || 1)) + "</b></td></tr>";
  }).join("");
  return "<table style='width:100%;border-collapse:collapse'>" + rows + "</table>";
}
function shell(title, html) {
  return "<div style='background:#fff8ec;font-family:Arial,Helvetica,sans-serif;color:#3d2c1e;padding:24px'>" +
    "<div style='max-width:560px;margin:auto;background:#ffffff;border-radius:16px;overflow:hidden;border:1px solid #f0e2cc'>" +
    "<div style='background:linear-gradient(135deg,#f5471f,#f9c74f);color:#fff;padding:22px 26px;display:flex;align-items:center;gap:12px'>" +
    "<div style='font-size:26px'>ॐ</div><div><div style='font-size:22px;font-weight:bold'>Pipilika</div>" +
    "<div style='font-size:12px;letter-spacing:2px;text-transform:uppercase'>Pipli Appliqué</div></div></div>" +
    "<div style='padding:26px'>" +
    "<h1 style='font-size:20px;margin:0 0 6px;color:#f5471f'>" + title + "</h1>" +
    html +
    "</div>" +
    "<div style='padding:14px 26px;background:#fff0d8;font-size:12px;color:#6b5540;text-align:center'>" +
    "Handcrafted with care in Pipili, Odisha · Questions? Reply to this email.</div></div></div>";
}

/* ---------- Amazon-style order confirmation for the BUYER ---------- */
function buyerEmail(order) {
  const trackUrl = (process.env.WEBSITE_URL || "http://localhost:3000") + "/index.html#track";
  return {
    subject: "Order Confirmed " + order.id + " — Pipilika Handicrafts",
    html: shell("Order Confirmed — Thank you, " + esc(order.name.split(" ")[0]) + "!", "" +
      "<p style='color:#6b5540;font-size:14px'>Great news! Your handcrafted Pipli piece is confirmed. We've notified our artisans who will begin making it right away. A copy of this email goes to " + esc(order.email) + ".</p>" +
      "<div style='background:#f6fbee;border:1px solid #d7e8c8;border-radius:12px;padding:16px 18px;margin:18px 0'>" +
      "<div style='font-size:13px;color:#2b8a3e;font-weight:bold'>ORDER NUMBER</div>" +
      "<div style='font-size:22px;font-weight:bold;letter-spacing:1px'>" + esc(order.id) + "</div>" +
      "<div style='font-size:13px;color:#6b5540;margin-top:6px'>Estimated delivery: <b>" + order.days + " day" + (order.days === 1 ? "" : "s") + "</b> · arrives " + datePlus(order.date, order.days) + "</div>" +
      "</div>" +
      "<h3 style='font-size:15px;margin:0 0 6px'>Items</h3>" + itemsList(order) +
      "<hr style='border:none;border-top:1px dashed #e0d2bd;margin:14px 0'/>" +
      "<div style='display:flex;justify-content:space-between;font-size:15px'><span>Order Total</span><b style='color:#f5471f;font-size:18px'>" + fmt(order.total) + "</b></div>" +
      "<div style='font-size:13px;color:#6b5540;margin-top:14px'>Delivering to: <b>" + esc(order.addr) + "</b></div>" +
      "<div style='margin:20px 0 4px'><a href='" + esc(trackUrl) + "' style='background:#f5471f;color:#fff;text-decoration:none;padding:12px 22px;border-radius:999px;font-weight:bold;display:inline-block'>Track your delivery</a></div>")
  };
}

/* ---------- Congratulations note for the COMPANY ---------- */
function companyEmail(order) {
  return {
    subject: "🎉 NEW ORDER " + order.id + " — Congratulations!",
    html: shell("New Order — Congratulations!", "" +
      "<p style='color:#6b5540;font-size:14px'>A customer just ordered with us. Details below.</p>" +
      "<div style='background:#fff3e0;border:1px solid #f9d7a0;border-radius:12px;padding:16px 18px;margin:14px 0'>" +
      "<div style='font-size:13px;color:#b7791f;font-weight:bold'>ORDER NUMBER</div>" +
      "<div style='font-size:22px;font-weight:bold'>" + esc(order.id) + "</div></div>" +
      "<table style='width:100%;font-size:14px;border-collapse:collapse'>" +
      tr("Customer", esc(order.name)) +
      tr("Buyer email", esc(order.email)) +
      tr("City / Country", esc(order.city) + ", " + esc(order.country)) +
      tr("Delivery days", String(order.days)) +
      tr("Shipping to", esc(order.addr)) +
      "</table>" +
      "<h3 style='font-size:15px;margin:16px 0 6px'>Items</h3>" + itemsList(order) +
      "<hr style='border:none;border-top:1px dashed #e0d2bd;margin:14px 0'/>" +
      "<div style='display:flex;justify-content:space-between;font-size:15px'><span>Order Total</span><b style='color:#f5471f;font-size:18px'>" + fmt(order.total) + "</b></div>")
  };
}
function tr(k, v) {
  return "<tr><td style='padding:6px 0;color:#6b5540;width:42%'>" + k + "</td><td style='padding:6px 0;font-weight:600'>" + v + "</td></tr>";
}

/* Sends the buyer confirmation + company notification. Throws on failure. */
async function sendOrderEmails(order) {
  const results = [];
  const buyer = await transport.sendMail(Object.assign({
    from: process.env.SMTP_FROM,
    to: order.email
  }, buyerEmail(order)));
  results.push("buyer:" + buyer.messageId);

  if (process.env.COMPANY_EMAIL) {
    const comp = await transport.sendMail(Object.assign({
      from: process.env.SMTP_FROM,
      to: process.env.COMPANY_EMAIL
    }, companyEmail(order)));
    results.push("company:" + comp.messageId);
  }
  return results;
}

module.exports = { sendOrderEmails, transport };