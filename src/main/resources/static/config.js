
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