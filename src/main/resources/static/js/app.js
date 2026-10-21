/* Poggers — Marketplace de Eletrônicos · frontend (JS puro) */
"use strict";

/* ============ utilidades ============ */
const $ = (s, r = document) => r.querySelector(s);
const $$ = (s, r = document) => [...r.querySelectorAll(s)];
const esc = (s) => String(s ?? "").replace(/[&<>"']/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
const brl = (v) => Number(v || 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
const norm = (s) => String(s ?? "").normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase();
const debounce = (fn, ms) => { let t; return (...a) => { clearTimeout(t); t = setTimeout(() => fn(...a), ms); }; };
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

function parseDate(v) {
  if (Array.isArray(v)) { const [y, m, d, h = 0, mi = 0, s = 0] = v; return new Date(y, m - 1, d, h, mi, s); }
  return v ? new Date(v) : null;
}
function fmtDate(v) {
  const d = parseDate(v);
  return d && !isNaN(d) ? d.toLocaleString("pt-BR", { dateStyle: "short", timeStyle: "short" }) : "";
}
const store = {
  get(k, d = null) { try { const v = localStorage.getItem(k); return v ? JSON.parse(v) : d; } catch { return d; } },
  set(k, v) { try { localStorage.setItem(k, JSON.stringify(v)); } catch { /* ignora */ } },
  del(k) { try { localStorage.removeItem(k); } catch { /* ignora */ } },
};

const ICON = {
  bag: '<svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M5 8h14l-1.3 11H6.3z"/><path d="M9 8a3 3 0 0 1 6 0"/></svg>',
  search: '<svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"><circle cx="11" cy="11" r="7"/><path d="m20 20-3.5-3.5"/></svg>',
  alert: '<svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M12 3 2 20h20z"/><path d="M12 10v4M12 17.5v.01"/></svg>',
  box: '<svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="m21 8-9-5-9 5v8l9 5 9-5z"/><path d="m3 8 9 5 9-5M12 13v8"/></svg>',
  user: '<svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"><circle cx="12" cy="8" r="4"/><path d="M4 21a8 8 0 0 1 16 0"/></svg>',
  close: '<svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M6 6l12 12M18 6 6 18"/></svg>',
  plus: '<svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round"><path d="M12 5v14M5 12h14"/></svg>',
  trash: '<svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 7h16M10 11v6M14 11v6M6 7l1 13h10l1-13M9 7V4h6v3"/></svg>',
  edit: '<svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 20h4L19 9l-4-4L4 16z"/></svg>',
};

/* ============ estado ============ */
const state = {
  user: store.get("mercado_user"),
  products: [],
  categories: [],
  catalogStatus: "loading", // loading | ready | error
  catalogError: "",
  filters: { q: "", cat: "", stock: false, sort: "default" },
  cart: null,
  busy: false,
  pending: null, // ação a repetir depois do login
};
const isSeller = () => !!state.user && store.get(`mercado_seller_${state.user.id}`) === true;
const setSeller = (v) => state.user && (v ? store.set(`mercado_seller_${state.user.id}`, true) : store.del(`mercado_seller_${state.user.id}`));
const productById = (id) => state.products.find((p) => p.id === Number(id));

/* ============ UI base: toast, modal, drawer ============ */
function toast(msg, type = "") {
  const el = document.createElement("div");
  el.className = `toast ${type}`;
  el.textContent = msg;
  const box = $("#toasts");
  box.append(el);
  while (box.children.length > 2) box.firstElementChild.remove(); // no máximo 2 avisos empilhados
  setTimeout(() => { el.classList.add("out"); setTimeout(() => el.remove(), 300); }, type === "error" ? 5000 : 2800);
}
const fail = (e) => toast(e?.message || "Algo deu errado.", "error");

let lastFocus = null;
function openModal(html, { wide = false } = {}) {
  lastFocus = document.activeElement;
  const m = $("#modal");
  m.className = `modal${wide ? " wide" : ""}`;
  m.innerHTML = `<button class="icon-btn modal-close" data-action="close-modal" aria-label="Fechar">${ICON.close}</button>${html}`;
  $("#modalWrap").hidden = false;
  document.body.style.overflow = "hidden";
  const first = $("input, select, textarea", m) || $("button.btn", m);
  setTimeout(() => first?.focus(), 30);
}
function closeModal() {
  if ($("#modalWrap").hidden) return;
  $("#modalWrap").hidden = true;
  $("#modal").innerHTML = "";
  $("#modalWrap").dispatchEvent(new Event("modal-closed"));
  if ($("#drawer").classList.contains("open")) return;
  document.body.style.overflow = "";
  lastFocus?.focus?.();
}
function confirmDialog({ title, message, ok = "Confirmar", danger = false }) {
  return new Promise((resolve) => {
    openModal(`<div class="modal-pad"><h3>${esc(title)}</h3><p class="sub">${esc(message)}</p>
      <div class="form-actions"><button class="btn btn-ghost" data-confirm="0">Voltar</button>
      <button class="btn ${danger ? "btn-danger" : ""}" data-confirm="1">${esc(ok)}</button></div></div>`);
    const m = $("#modal");
    const done = (v) => { m.removeEventListener("click", onClick); $("#modalWrap").removeEventListener("modal-closed", onClose); resolve(v); };
    const onClick = (e) => { const b = e.target.closest("[data-confirm]"); if (b) { done(b.dataset.confirm === "1"); closeModal(); } };
    const onClose = () => done(false);
    m.addEventListener("click", onClick);
    $("#modalWrap").addEventListener("modal-closed", onClose, { once: true });
  });
}

function openCart() {
  $("#drawer").classList.add("open");
  $("#drawer").setAttribute("aria-hidden", "false");
  $("#scrim").hidden = false;
  document.body.style.overflow = "hidden";
  renderCart();
  if (state.user) refreshCart().then(renderCart).catch(fail);
}
function closeCart() {
  $("#drawer").classList.remove("open");
  $("#drawer").setAttribute("aria-hidden", "true");
  $("#scrim").hidden = true;
  if ($("#modalWrap").hidden) document.body.style.overflow = "";
}

/* ============ visual dos produtos (a API não tem imagem) ============ */
const PALETTES = [["#fde7c8", "#8a4b08"], ["#dcefe3", "#1f6b4a"], ["#fbd9d0", "#b23a18"], ["#e1e9f7", "#2a4f8f"], ["#efe3f5", "#6b3a8a"], ["#fff3c4", "#7a5b00"], ["#d9f0ef", "#17696a"], ["#f6dbe4", "#9b2c56"]];
function tile(p, { tag = false, flag = false } = {}) {
  const [bg, fg] = PALETTES[(Number(p.categoryId) || 0) % PALETTES.length];
  const out = p.quantity <= 0;
  const low = !out && p.quantity <= 5;
  return `<div class="tile" style="--t-bg:${bg};--t-fg:${fg}" aria-hidden="true">
    ${tag ? `<span class="tag">${esc(p.categoryName || "Geral")}</span>` : ""}
    ${flag && (out || low) ? `<span class="stock-flag ${out ? "out" : ""}">${out ? "Esgotado" : `Restam ${p.quantity}`}</span>` : ""}
    <span>${esc((p.name || "?").trim().charAt(0).toUpperCase())}</span></div>`;
}

/* ============ dados ============ */
async function loadCatalog() {
  state.catalogStatus = "loading";
  try {
    const [products, categories] = await Promise.all([API.products(), API.categories().catch(() => [])]);
    state.products = products || [];
    state.categories = categories || [];
    state.catalogStatus = "ready";
  } catch (e) {
    state.catalogStatus = "error";
    state.catalogError = e.message;
  }
}
async function refreshCart() {
  if (!state.user) { state.cart = null; return; }
  try {
    state.cart = await API.cart(state.user.id);
  } catch (e) {
    if (e.status !== 404) throw e;
    // Usuário ainda não é cliente: habilita o perfil e cria o carrinho.
    await API.becomeClient(state.user.id);
    state.cart = await API.cart(state.user.id);
  } finally {
    updateCartBadge();
  }
}
function updateCartBadge() {
  const n = state.cart?.itemQuantity ?? 0;
  const b = $("#cartCount");
  const changed = b.textContent !== String(n);
  b.textContent = n;
  b.hidden = n === 0;
  if (changed && n > 0) { b.classList.remove("bump"); void b.offsetWidth; b.classList.add("bump"); }
}

/* ============ sessão ============ */
function setUser(u) {
  state.user = u;
  if (u) store.set("mercado_user", u); else store.del("mercado_user");
  renderAccount();
}
function renderAccount() {
  const b = $("#accountBtn");
  if (state.user) {
    b.dataset.action = "account";
    b.innerHTML = `<span>${esc(state.user.username)}</span>`;
    b.title = "Minha conta";
  } else {
    b.dataset.action = "login";
    b.textContent = "Entrar";
    b.title = "";
  }
}
function logout() {
  setUser(null);
  state.cart = null;
  updateCartBadge();
  closeModal();
  closeCart();
  toast("Você saiu da conta.");
  if (route() !== "catalog") location.hash = "#/"; else render();
}
function requireLogin(pending = null, msg = "Entre na sua conta para continuar.") {
  state.pending = pending;
  openAuthModal("login", msg);
}

function openAuthModal(tab = "login", msg = "") {
  const isLogin = tab === "login";
  openModal(`<div class="modal-pad">
    <h3>${isLogin ? "Bem-vindo de volta" : "Crie sua conta"}</h3>
    <p class="sub">${esc(msg) || (isLogin ? "Entre para comprar e acompanhar seus pedidos." : "Leva menos de um minuto.")}</p>
    <div class="tabs" role="tablist">
      <button role="tab" aria-selected="${isLogin}" data-action="auth-tab" data-tab="login">Entrar</button>
      <button role="tab" aria-selected="${!isLogin}" data-action="auth-tab" data-tab="register">Criar conta</button>
    </div>
    <form class="form" data-form="${isLogin ? "login" : "register"}" novalidate>
      ${isLogin ? "" : `<div class="field"><label for="f-user">Nome de usuário</label><input id="f-user" name="username" maxlength="50" autocomplete="username" required></div>`}
      <div class="field"><label for="f-email">E-mail</label><input id="f-email" name="email" type="email" autocomplete="email" required></div>
      <div class="field"><label for="f-pass">Senha</label><input id="f-pass" name="password" type="password" ${isLogin ? "" : 'minlength="6"'} autocomplete="${isLogin ? "current-password" : "new-password"}" required>
        ${isLogin ? "" : '<span class="hint">Mínimo de 6 caracteres.</span>'}</div>
      <div class="form-error" hidden></div>
      <button class="btn btn-block" type="submit">${isLogin ? "Entrar" : "Criar conta e entrar"}</button>
    </form></div>`);
}

async function afterLogin(user) {
  setUser(user);
  closeModal();
  toast(`Olá, ${user.username}!`, "success");
  try { await refreshCart(); } catch (e) { fail(e); }
  const p = state.pending; state.pending = null;
  if (p) await p();
  render();
}

/* ============ roteamento ============ */
const ROUTES = { "": "catalog", pedidos: "orders", vender: "sell" };
const route = () => ROUTES[location.hash.replace(/^#\/?/, "")] || "catalog";

async function render() {
  const r = route();
  $$(".nav a").forEach((a) => (a.dataset.route === r ? a.setAttribute("aria-current", "page") : a.removeAttribute("aria-current")));
  $("#searchBox").style.visibility = r === "catalog" ? "visible" : "hidden";
  if (r === "catalog") renderCatalog();
  else if (!state.user) renderLoginWall(r);
  else if (r === "orders") await renderOrders();
  else if (r === "sell") await renderSell();
}

function stateBlock({ icon, title, text, action = "", error = false }) {
  return `<div class="state ${error ? "is-error" : ""}"><div class="state-icon">${icon}</div><h3>${esc(title)}</h3><p>${esc(text)}</p>${action}</div>`;
}
function renderLoginWall(r) {
  $("#view").innerHTML = stateBlock({
    icon: ICON.user,
    title: r === "orders" ? "Veja seus pedidos" : "Venda na banca",
    text: r === "orders" ? "Entre na sua conta para acompanhar o status de cada compra." : "Entre na sua conta para cadastrar e gerenciar seus produtos.",
    action: `<button class="btn" data-action="login">Entrar ou criar conta</button>`,
  });
}

/* ============ catálogo ============ */
function filteredProducts() {
  const { q, cat, stock, sort } = state.filters;
  let list = state.products.filter((p) =>
    (!cat || String(p.categoryId) === String(cat)) && (!stock || p.quantity > 0) && (!q || norm(p.name).includes(norm(q)) || norm(p.categoryName).includes(norm(q))));
  const by = { low: (a, b) => a.value - b.value, high: (a, b) => b.value - a.value, name: (a, b) => a.name.localeCompare(b.name, "pt-BR") }[sort];
  if (by) list = [...list].sort(by);
  return list;
}

function renderCatalog() {
  const v = $("#view");
  const cats = state.categories.length ? state.categories : [...new Map(state.products.map((p) => [p.categoryId, { id: p.categoryId, name: p.categoryName }])).values()];
  const f = state.filters;
  v.innerHTML = `
    <section class="hero">
      <span class="eyebrow">Poggers tech · tecnologia sem complicação</span>
      <h1>Tecnologia que acompanha <em>você.</em></h1>
      <p>Descubra eletrônicos para trabalhar, jogar e curtir. Encontre seu próximo favorito e compre em poucos cliques.</p>
      <div class="hero-stats">
        <div><b class="num">${state.catalogStatus === "ready" ? state.products.length : "—"}</b><span>produtos</span></div>
        <div><b class="num">${state.catalogStatus === "ready" ? cats.length : "—"}</b><span>categorias</span></div>
      </div>
    </section>
    <div class="toolbar">
      <div class="chips" role="group" aria-label="Categorias">
        <button class="chip" data-action="cat" data-id="" aria-pressed="${!f.cat}">Todos</button>
        ${cats.map((c) => `<button class="chip" data-action="cat" data-id="${c.id}" aria-pressed="${String(f.cat) === String(c.id)}">${esc(c.name)}</button>`).join("")}
      </div>
      <div class="tools">
        <label class="switch"><input type="checkbox" id="fStock" ${f.stock ? "checked" : ""}> Só em estoque</label>
        <select class="select" id="fSort" aria-label="Ordenar">
          ${[["default", "Ordem padrão"], ["low", "Menor preço"], ["high", "Maior preço"], ["name", "Nome (A–Z)"]].map(([k, l]) => `<option value="${k}" ${f.sort === k ? "selected" : ""}>${l}</option>`).join("")}
        </select>
      </div>
    </div>
    <div id="gridArea"></div>`;
  renderGrid();
}

function renderGrid() {
  const area = $("#gridArea");
  if (!area) return;
  if (state.catalogStatus === "loading") {
    area.innerHTML = `<div class="grid">${Array(8).fill(`<div class="card skeleton"><div class="tile"></div><div class="card-body"><div class="sk-line" style="width:70%"></div><div class="sk-line" style="width:40%"></div></div></div>`).join("")}</div>`;
    return;
  }
  if (state.catalogStatus === "error") {
    area.innerHTML = stateBlock({ icon: ICON.alert, error: true, title: "Não conseguimos carregar os produtos", text: state.catalogError, action: `<button class="btn" data-action="retry">Tentar de novo</button>` });
    return;
  }
  const list = filteredProducts();
  const filtering = state.filters.q || state.filters.cat || state.filters.stock;
  if (!list.length) {
    area.innerHTML = filtering
      ? stateBlock({ icon: ICON.search, title: "Nada por aqui", text: "Nenhum produto bate com os filtros escolhidos.", action: `<button class="btn btn-ghost" data-action="clear-filters">Limpar filtros</button>` })
      : stateBlock({ icon: ICON.box, title: "Seu próximo eletrônico está chegando", text: "Ainda não há produtos cadastrados no catálogo. Se você é vendedor, cadastre o primeiro item da loja.", action: `<a class="btn" href="#/vender">Anunciar produto</a>` });
    return;
  }
  area.innerHTML = `<p class="count" aria-live="polite">${list.length} ${list.length === 1 ? "produto" : "produtos"}</p>
    <div class="grid">${list.map((p) => `
      <article class="card ${p.quantity <= 0 ? "is-out" : ""}">
        <button class="card-media" data-action="open-product" data-id="${p.id}" aria-label="Ver ${esc(p.name)}">${tile(p, { tag: true, flag: true })}</button>
        <div class="card-body">
          <button class="card-title" data-action="open-product" data-id="${p.id}">${esc(p.name)}</button>
          <span class="card-meta">${p.quantity > 0 ? `${p.quantity} em estoque` : "Indisponível"}</span>
          <div class="card-foot">
            <span class="price num">${brl(p.value)}</span>
            <button class="btn btn-sm" data-action="add" data-id="${p.id}" ${p.quantity <= 0 ? "disabled" : ""} aria-label="Adicionar ${esc(p.name)} ao carrinho">${ICON.plus} Adicionar</button>
          </div>
        </div>
      </article>`).join("")}</div>`;
}

/* detalhe do produto */
async function openProduct(id) {
  const p = productById(id);
  if (!p) return;
  const out = p.quantity <= 0;
  openModal(`<div class="detail">
      ${tile(p, { tag: true })}
      <div class="detail-body">
        <div><h3>${esc(p.name)}</h3><span class="muted">${esc(p.categoryName || "Geral")}</span></div>
        <div class="price num">${brl(p.value)}</div>
        <span class="pill ${out ? "bad" : p.quantity <= 5 ? "warn" : "ok"}">${out ? "Esgotado" : p.quantity <= 5 ? `Últimas ${p.quantity} unidades` : `${p.quantity} em estoque`}</span>
        <div class="suppliers" id="suppliers" hidden></div>
        <div class="buy-row">
          <div class="stepper" ${out ? "hidden" : ""}>
            <button type="button" data-action="qty-dec" aria-label="Diminuir">−</button>
            <output id="qtyOut" aria-live="polite">1</output>
            <button type="button" data-action="qty-inc" data-max="${p.quantity}" aria-label="Aumentar">+</button>
          </div>
          <button class="btn" data-action="add-detail" data-id="${p.id}" ${out ? "disabled" : ""}>${out ? "Indisponível" : "Adicionar ao carrinho"}</button>
        </div>
      </div></div>`, { wide: true });
  try {
    const sup = await API.productSuppliers(p.id);
    const box = $("#suppliers");
    if (box && sup?.length) {
      box.hidden = false;
      box.innerHTML = `<h4>Fornecedores</h4><ul>${sup.map((s) => `<li><span>${esc(s.supplierName)}</span><span class="muted">entrega em ${s.deliveryTime} ${s.deliveryTime === 1 ? "dia" : "dias"}</span></li>`).join("")}</ul>`;
    }
  } catch { /* fornecedores são opcionais */ }
}

/* ============ carrinho ============ */
async function addToCart(productId, qty = 1) {
  if (!state.user) return requireLogin(() => addToCart(productId, qty), "Entre na sua conta para adicionar ao carrinho.");
  try {
    if (!state.cart) await refreshCart();
    state.cart = await API.addCartItem(state.cart.id, Number(productId), qty);
    updateCartBadge();
    const p = productById(productId);
    toast(`${p?.name ?? "Produto"} adicionado ao carrinho`, "success");
    if ($("#drawer").classList.contains("open")) renderCart();
  } catch (e) { fail(e); }
}

async function changeQty(productId, delta) {
  if (state.busy || !state.cart) return;
  const item = state.cart.items.find((i) => i.productId === Number(productId));
  if (!item) return;
  state.busy = true; renderCart();
  try {
    const cartId = state.cart.id;
    if (delta > 0) {
      state.cart = await API.addCartItem(cartId, item.productId, 1);
    } else if (item.quantity === 1) {
      state.cart = await API.removeCartItem(cartId, item.productId);
    } else { // a API não tem "atualizar quantidade": remove e adiciona de novo com a nova quantidade
      await API.removeCartItem(cartId, item.productId);
      state.cart = await API.addCartItem(cartId, item.productId, item.quantity - 1);
    }
  } catch (e) { fail(e); try { await refreshCart(); } catch { /* ignora */ } }
  state.busy = false; updateCartBadge(); renderCart();
}
async function removeItem(productId) {
  if (state.busy || !state.cart) return;
  state.busy = true; renderCart();
  try { state.cart = await API.removeCartItem(state.cart.id, Number(productId)); } catch (e) { fail(e); }
  state.busy = false; updateCartBadge(); renderCart();
}

function renderCart() {
  const d = $("#drawer");
  const head = `<div class="drawer-head"><h3>Seu carrinho</h3><button class="icon-btn" data-action="close-cart" aria-label="Fechar carrinho">${ICON.close}</button></div>`;
  if (!state.user) {
    d.innerHTML = head + `<div class="drawer-body">${stateBlock({ icon: ICON.user, title: "Entre para começar", text: "Faça login para montar seu carrinho.", action: `<button class="btn" data-action="login">Entrar</button>` })}</div>`;
    return;
  }
  const items = state.cart?.items ?? [];
  if (!items.length) {
    d.innerHTML = head + `<div class="drawer-body">${stateBlock({ icon: ICON.bag, title: "Carrinho vazio", text: "Adicione produtos do catálogo para vê-los aqui.", action: `<button class="btn btn-ghost" data-action="close-cart">Continuar comprando</button>` })}</div>`;
    return;
  }
  d.innerHTML = head + `
    <div class="drawer-body">${items.map((i) => {
      const p = productById(i.productId) || { name: i.productName, categoryId: 0, quantity: 99 };
      return `<div class="line-item">
        ${tile({ ...p, name: i.productName })}
        <div><div class="line-name">${esc(i.productName)}</div><div class="line-sub num">${brl(i.unitValue)} cada</div>
          <button class="link-btn" data-action="cart-remove" data-id="${i.productId}" ${state.busy ? "disabled" : ""}>Remover</button></div>
        <div class="line-right">
          <div class="stepper"><button data-action="cart-dec" data-id="${i.productId}" aria-label="Diminuir" ${state.busy ? "disabled" : ""}>−</button>
            <output>${i.quantity}</output>
            <button data-action="cart-inc" data-id="${i.productId}" aria-label="Aumentar" ${state.busy || i.quantity >= p.quantity ? "disabled" : ""}>+</button></div>
          <b class="num">${brl(i.subtotal)}</b></div></div>`;
    }).join("")}</div>
    <div class="drawer-foot">
      <div class="sum"><span class="muted">${state.cart.itemQuantity} ${state.cart.itemQuantity === 1 ? "item" : "itens"} · Total</span><b class="num">${brl(state.cart.totalValue)}</b></div>
      <button class="btn btn-block" data-action="checkout" ${state.busy ? "disabled" : ""}>Finalizar pedido</button>
    </div>`;
}

async function checkout() {
  if (state.busy || !state.cart?.items?.length) return;
  state.busy = true; renderCart();
  try {
    const order = await API.checkout(state.cart.id);
    await refreshCart();
    await loadCatalog(); // o estoque mudou
    state.busy = false;
    closeCart();
    toast(`Pedido #${order.id} criado com sucesso!`, "success");
    location.hash = "#/pedidos";
    if (route() === "orders") render();
  } catch (e) {
    state.busy = false;
    fail(e);
    renderCart();
  }
}

/* ============ pedidos ============ */
const STATUS = {
  ABERTO: { label: "Aguardando pagamento", pill: "warn" },
  PAGO: { label: "Pago", pill: "info" },
  ENVIADO: { label: "Enviado", pill: "info" },
  ENTREGUE: { label: "Entregue", pill: "ok" },
  CANCELADO: { label: "Cancelado", pill: "bad" },
};
const STEPS = [["ABERTO", "Pedido feito"], ["PAGO", "Pago"], ["ENVIADO", "Enviado"], ["ENTREGUE", "Entregue"]];

async function renderOrders() {
  const v = $("#view");
  v.innerHTML = `<div class="page-head"><div><h2>Meus pedidos</h2><p>Acompanhe o andamento de cada compra.</p></div></div>
    <div class="orders">${`<div class="order skeleton"><div class="sk-line" style="width:30%;height:18px"></div><div class="sk-line" style="margin-top:16px"></div><div class="sk-line" style="margin-top:10px;width:60%"></div></div>`.repeat(2)}</div>`;
  let orders;
  try { orders = await API.orders(state.user.id); }
  catch (e) {
    if (e.status === 404 || e.status === 422) orders = []; // ainda não é cliente
    else { v.innerHTML = stateBlock({ icon: ICON.alert, error: true, title: "Não foi possível carregar os pedidos", text: e.message, action: `<button class="btn" data-action="retry-route">Tentar de novo</button>` }); return; }
  }
  if (route() !== "orders") return;
  if (!orders.length) {
    v.innerHTML = `<div class="page-head"><div><h2>Meus pedidos</h2></div></div>` + stateBlock({ icon: ICON.bag, title: "Nenhum pedido ainda", text: "Quando você finalizar uma compra, ela aparece aqui.", action: `<a class="btn" href="#/">Ir às compras</a>` });
    return;
  }
  v.innerHTML = `<div class="page-head"><div><h2>Meus pedidos</h2><p>${orders.length} ${orders.length === 1 ? "pedido" : "pedidos"}</p></div></div>
    <div class="orders">${orders.map(orderCard).join("")}</div>`;
}

function orderCard(o) {
  const st = STATUS[o.currentStatus] || { label: o.currentStatus, pill: "" };
  const hist = new Map((o.history || []).map((h) => [h.status, h.date]));
  const cancelled = o.currentStatus === "CANCELADO";
  const actions = [];
  if (o.currentStatus === "ABERTO") actions.push(`<button class="btn btn-sm" data-action="order-status" data-id="${o.id}" data-status="PAGO">Pagar agora</button>`);
  if (o.currentStatus === "ENVIADO") actions.push(`<button class="btn btn-sm" data-action="order-status" data-id="${o.id}" data-status="ENTREGUE">Confirmar entrega</button>`);
  if (["ABERTO", "PAGO"].includes(o.currentStatus)) actions.push(`<button class="btn btn-sm btn-danger" data-action="order-cancel" data-id="${o.id}">Cancelar pedido</button>`);
  return `<article class="order">
    <div class="order-head"><div><h3>Pedido #${o.id}</h3><span class="muted">${fmtDate(o.dateOrder)}</span></div><span class="pill ${st.pill}">${esc(st.label)}</span></div>
    <div class="timeline ${cancelled ? "cancelled" : ""}" aria-label="Andamento do pedido">
      ${STEPS.map(([k, l]) => `<div class="step ${hist.has(k) ? "done" : ""}">${l}${hist.has(k) ? `<br><small>${fmtDate(hist.get(k))}</small>` : ""}</div>`).join("")}
    </div>
    <div class="order-items">${(o.items || []).map((i) => `<div><span>${i.quantity}× ${esc(i.productName)}</span><span class="num muted">${brl(i.unitValue * i.quantity)}</span></div>`).join("")}</div>
    <div class="order-foot"><span class="total num">${brl(o.totalValue)}</span><div class="btn-row">${actions.join("")}</div></div>
  </article>`;
}

async function setOrderStatus(id, status) {
  try {
    await API.changeOrderStatus(id, status);
    toast(status === "PAGO" ? "Pagamento confirmado!" : status === "CANCELADO" ? "Pedido cancelado." : "Status atualizado.", "success");
    if (status === "CANCELADO") await loadCatalog(); // estoque devolvido
    await renderOrders();
  } catch (e) { fail(e); }
}

/* ============ vendedor ============ */
const maskCnpj = (v) => v.replace(/\D/g, "").slice(0, 14).replace(/^(\d{2})(\d)/, "$1.$2").replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3").replace(/\.(\d{3})(\d)/, ".$1/$2").replace(/(\d{4})(\d)/, "$1-$2");

async function renderSell() {
  const v = $("#view");
  if (!isSeller()) {
    v.innerHTML = `<div class="activate">${stateBlock({
      icon: ICON.box, title: "Comece a vender", text: "Informe o CNPJ do seu negócio para ativar sua conta de vendedor.",
      action: `<form class="form" data-form="activate" style="text-align:left" novalidate>
        <div class="field"><label for="f-cnpj">CNPJ</label><input id="f-cnpj" name="cnpj" inputmode="numeric" placeholder="00.000.000/0000-00" autocomplete="off" required></div>
        <div class="form-error" hidden></div>
        <button class="btn btn-block" type="submit">Ativar conta de vendedor</button>
        <button type="button" class="link-btn" data-action="already-seller" style="justify-self:center">Já sou vendedor</button></form>` })}</div>`;
    return;
  }
  v.innerHTML = `<div class="page-head"><div><h2>Meus produtos</h2><p>Gerencie o que você vende na banca.</p></div>
    <button class="btn" data-action="product-new">${ICON.plus} Novo produto</button></div><div id="sellArea"></div>`;
  $("#sellArea").innerHTML = `<div class="grid">${`<div class="card skeleton"><div class="tile"></div></div>`.repeat(3)}</div>`;
  let mine;
  try {
    if (!state.categories.length) state.categories = await API.categories().catch(() => []);
    mine = await API.products({ sellerId: state.user.id });
  } catch (e) { $("#sellArea").innerHTML = stateBlock({ icon: ICON.alert, error: true, title: "Erro ao carregar", text: e.message, action: `<button class="btn" data-action="retry-route">Tentar de novo</button>` }); return; }
  if (route() !== "sell") return;
  const stockUnits = mine.reduce((s, p) => s + p.quantity, 0);
  const stockValue = mine.reduce((s, p) => s + p.quantity * p.value, 0);
  $("#sellArea").innerHTML = !mine.length
    ? stateBlock({ icon: ICON.box, title: "Nenhum produto cadastrado", text: "Cadastre seu primeiro produto para ele aparecer no catálogo.", action: `<button class="btn" data-action="product-new">${ICON.plus} Cadastrar produto</button>` })
    : `<div class="stats">
        <div class="stat"><span>Produtos</span><b class="num">${mine.length}</b></div>
        <div class="stat"><span>Unidades em estoque</span><b class="num">${stockUnits}</b></div>
        <div class="stat"><span>Valor em estoque</span><b class="num">${brl(stockValue)}</b></div></div>
      <div class="table-wrap"><table><thead><tr><th>Produto</th><th>Categoria</th><th class="r">Preço</th><th class="r">Estoque</th><th class="r">Ações</th></tr></thead><tbody>
        ${mine.map((p) => `<tr>
          <td><div class="prod">${tile(p)}<span>${esc(p.name)}</span></div></td>
          <td class="muted">${esc(p.categoryName)}</td>
          <td class="r num">${brl(p.value)}</td>
          <td class="r"><span class="pill ${p.quantity <= 0 ? "bad" : p.quantity <= 5 ? "warn" : "ok"} num">${p.quantity <= 0 ? "Esgotado" : p.quantity}</span></td>
          <td class="r"><div class="btn-row" style="justify-content:flex-end">
            <button class="btn btn-sm btn-soft" data-action="product-edit" data-id="${p.id}" aria-label="Editar ${esc(p.name)}">${ICON.edit} Editar</button>
            <button class="btn btn-sm btn-danger" data-action="product-delete" data-id="${p.id}" aria-label="Excluir ${esc(p.name)}">${ICON.trash}</button></div></td></tr>`).join("")}
      </tbody></table></div>`;
}

function openProductForm(p = null) {
  const cats = state.categories;
  openModal(`<div class="modal-pad"><h3>${p ? "Editar produto" : "Novo produto"}</h3><p class="sub">${p ? "Atualize as informações do produto." : "Preencha os dados para publicar no catálogo."}</p>
    <form class="form" data-form="product" data-id="${p?.id ?? ""}" novalidate>
      <div class="field"><label for="p-name">Nome</label><input id="p-name" name="name" maxlength="100" value="${esc(p?.name ?? "")}" required></div>
      <div class="row-2">
        <div class="field"><label for="p-val">Preço (R$)</label><input id="p-val" name="value" type="number" min="0.01" step="0.01" inputmode="decimal" value="${p?.value ?? ""}" required></div>
        <div class="field"><label for="p-qty">Estoque</label><input id="p-qty" name="quantity" type="number" min="0" step="1" inputmode="numeric" value="${p?.quantity ?? ""}" required></div>
      </div>
      <div class="field"><label for="p-cat">Categoria</label>
        <div class="inline-add"><select id="p-cat" name="categoryId" required>${catOptions(cats, p?.categoryId)}</select>
        <button type="button" class="btn btn-ghost btn-sm" data-action="toggle-newcat">${ICON.plus} Nova</button></div></div>
      <div class="field" id="newCatBox" ${cats.length ? "hidden" : ""}>
        <label for="p-newcat">Nova categoria</label>
        <div class="inline-add"><input id="p-newcat" placeholder="Ex.: Hortifruti" maxlength="100"><button type="button" class="btn btn-soft btn-sm" data-action="create-cat">Criar</button></div>
        ${cats.length ? "" : '<span class="hint">Ainda não há categorias — crie a primeira.</span>'}</div>
      <div class="form-error" hidden></div>
      <div class="form-actions"><button type="button" class="btn btn-ghost" data-action="close-modal">Cancelar</button><button class="btn" type="submit">${p ? "Salvar alterações" : "Publicar produto"}</button></div>
    </form></div>`);
}
const catOptions = (cats, sel) => (cats.length ? cats.map((c) => `<option value="${c.id}" ${String(c.id) === String(sel) ? "selected" : ""}>${esc(c.name)}</option>`).join("") : `<option value="">— sem categorias —</option>`);

/* ============ formulários ============ */
async function handleForm(form) {
  const kind = form.dataset.form;
  const data = Object.fromEntries(new FormData(form));
  const err = $(".form-error", form);
  const btn = $('button[type="submit"]', form);
  const showErr = (m) => { err.textContent = m; err.hidden = false; };
  err.hidden = true;
  const label = btn.textContent; btn.disabled = true; btn.textContent = "Aguarde…";
  try {
    if (kind === "login") {
      if (!data.email || !data.password) return showErr("Preencha e-mail e senha.");
      await afterLogin(await API.login({ email: data.email.trim(), password: data.password }));
    } else if (kind === "register") {
      if (!data.username?.trim()) return showErr("Informe um nome de usuário.");
      if (!/^\S+@\S+\.\S+$/.test(data.email)) return showErr("Informe um e-mail válido.");
      if ((data.password || "").length < 6) return showErr("A senha precisa ter ao menos 6 caracteres.");
      await API.register({ username: data.username.trim(), email: data.email.trim(), password: data.password });
      await afterLogin(await API.login({ email: data.email.trim(), password: data.password }));
    } else if (kind === "activate") {
      const digits = (data.cnpj || "").replace(/\D/g, "");
      if (digits.length !== 14) return showErr("O CNPJ deve ter 14 dígitos.");
      try { await API.becomeSeller(state.user.id, digits); }
      catch (e) { if (!/já é vendedor/i.test(e.message)) throw e; }
      setSeller(true);
      toast("Conta de vendedor ativada!", "success");
      await renderSell();
    } else if (kind === "product") {
      if (!data.name?.trim()) return showErr("Informe o nome do produto.");
      if (!(Number(data.value) > 0)) return showErr("O preço deve ser maior que zero.");
      if (data.quantity === "" || Number(data.quantity) < 0) return showErr("Informe um estoque válido (0 ou mais).");
      if (!data.categoryId) return showErr("Escolha ou crie uma categoria.");
      const body = { name: data.name.trim(), value: Number(data.value), quantity: parseInt(data.quantity, 10), sellerId: state.user.id, categoryId: Number(data.categoryId) };
      const id = form.dataset.id;
      id ? await API.updateProduct(id, body) : await API.createProduct(body);
      closeModal();
      toast(id ? "Produto atualizado." : "Produto publicado no catálogo!", "success");
      await loadCatalog();
      await renderSell();
    }
  } catch (e) { showErr(e.message); }
  finally { if (btn.isConnected) { btn.disabled = false; btn.textContent = label; } }
}

/* ============ eventos ============ */
const actions = {
  login: () => openAuthModal("login"),
  account: () => openModal(`<div class="modal-pad"><h3>${esc(state.user.username)}</h3><p class="sub">${esc(state.user.email)}</p>
    <div class="form-actions" style="justify-content:space-between"><a class="btn btn-ghost" href="#/pedidos" data-action="close-modal">Meus pedidos</a><button class="btn btn-danger" data-action="logout">Sair da conta</button></div></div>`),
  logout,
  "auth-tab": (el) => openAuthModal(el.dataset.tab),
  "close-modal": closeModal,
  "close-cart": closeCart,
  retry: async () => { state.catalogStatus = "loading"; renderGrid(); await loadCatalog(); renderCatalog(); },
  "retry-route": render,
  cat: (el) => { state.filters.cat = el.dataset.id; renderCatalog(); },
  "clear-filters": () => { state.filters = { q: "", cat: "", stock: false, sort: "default" }; $("#search").value = ""; renderCatalog(); },
  "open-product": (el) => openProduct(el.dataset.id),
  add: (el) => addToCart(el.dataset.id, 1),
  "add-detail": async (el) => { const q = Number($("#qtyOut")?.textContent || 1); closeModal(); await addToCart(el.dataset.id, q); },
  "qty-inc": (el) => { const o = $("#qtyOut"); o.textContent = Math.min(Number(el.dataset.max), Number(o.textContent) + 1); },
  "qty-dec": () => { const o = $("#qtyOut"); o.textContent = Math.max(1, Number(o.textContent) - 1); },
  "cart-inc": (el) => changeQty(el.dataset.id, 1),
  "cart-dec": (el) => changeQty(el.dataset.id, -1),
  "cart-remove": (el) => removeItem(el.dataset.id),
  checkout,
  "order-status": (el) => setOrderStatus(el.dataset.id, el.dataset.status),
  "order-cancel": async (el) => { if (await confirmDialog({ title: "Cancelar pedido?", message: "Os itens voltam para o estoque. Essa ação não pode ser desfeita.", ok: "Cancelar pedido", danger: true })) setOrderStatus(el.dataset.id, "CANCELADO"); },
  "already-seller": () => { setSeller(true); renderSell(); },
  "product-new": () => openProductForm(),
  "product-edit": (el) => openProductForm(productById(el.dataset.id) || null),
  "product-delete": async (el) => {
    const p = productById(el.dataset.id);
    if (!(await confirmDialog({ title: "Excluir produto?", message: `“${p?.name ?? "Produto"}” será removido do catálogo.`, ok: "Excluir", danger: true }))) return;
    try { await API.deleteProduct(el.dataset.id); toast("Produto excluído.", "success"); await loadCatalog(); await renderSell(); } catch (e) { fail(e); }
  },
  "toggle-newcat": () => { const b = $("#newCatBox"); b.hidden = !b.hidden; if (!b.hidden) $("#p-newcat").focus(); },
  "create-cat": async () => {
    const name = $("#p-newcat").value.trim();
    if (!name) return $("#p-newcat").focus();
    try {
      const c = await API.createCategory({ name, description: null, parentId: null });
      state.categories.push(c);
      $("#p-cat").innerHTML = catOptions(state.categories, c.id);
      $("#p-newcat").value = ""; $("#newCatBox").hidden = true;
      toast(`Categoria “${c.name}” criada.`, "success");
    } catch (e) { fail(e); }
  },
};

document.addEventListener("click", (e) => {
  if (e.target === $("#modalWrap")) return closeModal();
  const el = e.target.closest("[data-action]");
  if (!el || el.disabled) return;
  const fn = actions[el.dataset.action];
  if (fn) Promise.resolve(fn(el)).catch(fail);
});
document.addEventListener("submit", (e) => {
  const form = e.target.closest("[data-form]");
  if (form) { e.preventDefault(); handleForm(form); }
});
document.addEventListener("input", (e) => {
  if (e.target.id === "f-cnpj") e.target.value = maskCnpj(e.target.value);
});
document.addEventListener("change", (e) => {
  if (e.target.id === "fStock") { state.filters.stock = e.target.checked; renderGrid(); }
  if (e.target.id === "fSort") { state.filters.sort = e.target.value; renderGrid(); }
});
document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    if (!$("#modalWrap").hidden) closeModal(); else if ($("#drawer").classList.contains("open")) closeCart();
  }
  if (e.key === "Tab" && !$("#modalWrap").hidden) { // mantém o foco dentro do modal
    const f = $$("a[href], button:not([disabled]), input, select, textarea", $("#modal")).filter((x) => !x.closest("[hidden]"));
    if (!f.length) return;
    const [first, last] = [f[0], f[f.length - 1]];
    if (e.shiftKey && document.activeElement === first) { e.preventDefault(); last.focus(); }
    else if (!e.shiftKey && document.activeElement === last) { e.preventDefault(); first.focus(); }
  }
});
$("#cartBtn").addEventListener("click", openCart);
$("#scrim").addEventListener("click", closeCart);
$("#search").addEventListener("input", debounce((e) => {
  state.filters.q = e.target.value.trim();
  if (route() !== "catalog") location.hash = "#/"; else renderGrid();
}, 120));
window.addEventListener("hashchange", () => { window.scrollTo(0, 0); render(); });

/* ============ início ============ */
(async function init() {
  renderAccount();
  updateCartBadge();
  render(); // mostra esqueleto
  await loadCatalog();
  if (route() === "catalog") renderCatalog();
  if (state.user) refreshCart().catch(() => { /* o erro aparece ao abrir o carrinho */ });
})();
