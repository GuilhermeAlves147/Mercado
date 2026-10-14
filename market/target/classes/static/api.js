/**
 * Camada fina sobre fetch(). Toda chamada HTTP do app passa por aqui,
 * então se o backend exigir header extra (token, etc.) é só mexer
 * em UMA função: request().
 */

const Api = (() => {
  async function request(path, { method = "GET", body, auth = false } = {}) {
    const headers = { "Content-Type": "application/json" };

    // Se seu backend usar autenticação (ex: Basic, Bearer token),
    // descomente e ajuste a linha abaixo.
    // if (auth) headers["Authorization"] = `Bearer ${Session.getToken()}`;

    const res = await fetch(`${CONFIG.API_BASE}${path}`, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });

    let data = null;
    const text = await res.text();
    if (text) {
      try {
        data = JSON.parse(text);
      } catch {
        data = text;
      }
    }

    if (!res.ok) {
      const message =
        (data && (data.message || data.error)) ||
        `Erro ${res.status} ao chamar ${path}`;
      throw new Error(message);
    }

    return data;
  }

  return {
    get: (path, opts) => request(path, { ...opts, method: "GET" }),
    post: (path, body, opts) => request(path, { ...opts, method: "POST", body }),
    put: (path, body, opts) => request(path, { ...opts, method: "PUT", body }),
    del: (path, opts) => request(path, { ...opts, method: "DELETE" }),
  };
})();

/**
 * Sessão do usuário logado (guardada no localStorage).
 * Ajuste os campos se o seu backend devolver nomes diferentes.
 */
const Session = (() => {
  const KEY = "market_session";

  function save(user) {
    localStorage.setItem(KEY, JSON.stringify(user));
  }
  function get() {
    const raw = localStorage.getItem(KEY);
    return raw ? JSON.parse(raw) : null;
  }
  function clear() {
    localStorage.removeItem(KEY);
  }
  function isLogged() {
    return !!get();
  }

  return { save, get, clear, isLogged };


})();
