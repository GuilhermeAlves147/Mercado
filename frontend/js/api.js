/** Camada fina sobre fetch(): todas as chamadas à API passam por aqui. */
const API = (() => {
  async function request(path, { method = "GET", body } = {}) {
    let res;
    try {
      res = await fetch(`${CONFIG.API_BASE}${path}`, {
        method,
        headers: body !== undefined ? { "Content-Type": "application/json" } : undefined,
        body: body !== undefined ? JSON.stringify(body) : undefined,
      });
    } catch {
      const err = new Error(`Não foi possível conectar à API (${CONFIG.API_BASE || window.location.origin}). Verifique se o backend está rodando.`);
      err.network = true;
      throw err;
    }

    const text = await res.text();
    let data = null;
    if (text) { try { data = JSON.parse(text); } catch { data = text; } }

    if (!res.ok) {
      // O backend responde {"error": "..."} ou {"campo": "mensagem"} (validação).
      let message = `Erro ${res.status}`;
      if (data && typeof data === "object") message = data.error || Object.values(data).join(" · ") || message;
      const err = new Error(message);
      err.status = res.status;
      throw err;
    }
    return data;
  }

  const qs = (params) => {
    const q = new URLSearchParams();
    Object.entries(params).forEach(([k, v]) => { if (v !== "" && v != null) q.set(k, v); });
    return q.toString() ? `?${q}` : "";
  };

  return {
    // usuários
    register: (d) => request("/users", { method: "POST", body: d }),
    login: (d) => request("/login", { method: "POST", body: d }),
    becomeSeller: (id, cnpj) => request(`/users/${id}/seller`, { method: "POST", body: { cnpj } }),
    becomeClient: (id, phone = null) => request(`/users/${id}/client`, { method: "POST", body: { phone } }),
    // catálogo
    products: (params = {}) => request(`/products${qs(params)}`),
    createProduct: (d) => request("/products", { method: "POST", body: d }),
    updateProduct: (id, d) => request(`/products/${id}`, { method: "PUT", body: d }),
    deleteProduct: (id) => request(`/products/${id}`, { method: "DELETE" }),
    productSuppliers: (id) => request(`/products/${id}/suppliers`),
    categories: () => request("/categories"),
    createCategory: (d) => request("/categories", { method: "POST", body: d }),
    // carrinho e pedidos
    cart: (clientId) => request(`/clients/${clientId}/cart`),
    addCartItem: (cartId, productId, quantity) => request(`/carts/${cartId}/items`, { method: "POST", body: { productId, quantity } }),
    removeCartItem: (cartId, productId) => request(`/carts/${cartId}/items/${productId}`, { method: "DELETE" }),
    checkout: (cartId) => request(`/carts/${cartId}/checkout`, { method: "POST" }),
    orders: (clientId) => request(`/clients/${clientId}/orders`),
    changeOrderStatus: (id, status) => request(`/orders/${id}/status`, { method: "PATCH", body: { status } }),
  };
})();
