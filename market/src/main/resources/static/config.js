/**
 * ==========================================================================
 * CONFIGURAÇÃO DA API
 * ==========================================================================
 * Como você não me mandou os Controllers do Spring, montei os endpoints
 * seguindo a convenção REST mais comum pra esse tipo de entidade
 * (tb_user, tb_product, tb_cart, tb_buyItem).
 *
 * Se algum endpoint no seu backend tiver um nome diferente, só mudar
 * aqui embaixo — o resto do front não precisa ser tocado.
 * ==========================================================================
 */

const CONFIG = {
  API_BASE: "http://localhost:8081",

  ENDPOINTS: {
    register: "/users",
    login: "/login",

    products: "/products",
    productById: (id) => `/products/${id}`,

    productsByUser: (idUser) => `/products/user/${idUser}`,

    cartByUser: (idUser) => `/carts/user/${idUser}`,
    carts: "/carts",
    cartById: (id) => `/carts/${id}`,
    cartItems: (idCart) => `/carts/${idCart}/items`,
    cartItem: (idCart, idProduct) =>
        `/carts/${idCart}/items/${idProduct}`,
  }
};