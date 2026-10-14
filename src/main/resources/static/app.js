

// ---------------------------------------------------------------------
// Estado do carrinho
// ---------------------------------------------------------------------

const Cart = {
  items: [],

  add(product, quantity = 1) {
    const existing = this.items.find(
        (i) => i.idProduct === product.idProduct
    );

    if (existing) {
      existing.quantity += quantity;
    } else {
      this.items.push({
        idProduct: product.idProduct,
        name: product.name,
        unitValue: product.value,
        quantity,
      });
    }

    render();
  },

  updateQty(idProduct, quantity) {
    const item = this.items.find(
        (i) => i.idProduct === idProduct
    );

    if (!item) return;

    if (quantity <= 0) {
      this.items = this.items.filter(
          (i) => i.idProduct !== idProduct
      );
    } else {
      item.quantity = quantity;
    }

    render();
  },

  clear() {
    this.items = [];
    render();
  },

  totalValue() {
    return this.items.reduce(
        (sum, i) => sum + i.unitValue * i.quantity,
        0
    );
  },

  totalItems() {
    return this.items.reduce(
        (sum, i) => sum + i.quantity,
        0
    );
  },
};

let allProducts = [];


function toast(message, tone = "info") {
  const el = document.getElementById("toast");

  if (!el) {
    alert(message);
    return;
  }

  el.textContent = message;
  el.className = `toast show tone-${tone}`;

  clearTimeout(toast._t);

  toast._t = setTimeout(() => {
    el.classList.remove("show");
  }, 3200);
}

function money(v) {
  return Number(v).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
}

// ---------------------------------------------------------------------
// AUTENTICAÇÃO
// ---------------------------------------------------------------------

// LOGIN
async function handleLogin(e) {
  e.preventDefault();

  const email = document
      .getElementById("login-email")
      .value
      .trim();

  const password = document
      .getElementById("login-password")
      .value;

  if (!email || !password) {
    toast("Preencha e-mail e senha.", "error");
    return;
  }

  try {
    console.log("Tentando login:", email);

    const data = await Api.post(
        CONFIG.ENDPOINTS.login,
        {
          email: email,
          password: password,
        }
    );

    console.log("Resposta do login:", data);

    Session.save({
      idUser: data.idUser ?? data.id_user ?? data.id,
      user: data.name ?? data.user ?? data.email ?? email,
      email: data.email ?? email,
    });

    toast(
        `Bem-vindo(a), ${data.name ?? email}!`,
        "success"
    );

    closeModal("modal-login");

    refreshAuthUI();

    loadMyProducts();

  } catch (err) {
    console.error("Erro no login:", err);

    toast(
        err.message || "E-mail ou senha incorretos.",
        "error"
    );
  }
}


// CADASTRO
async function handleRegister(e) {
  e.preventDefault();

  const name = document
      .getElementById("register-name")
      .value
      .trim();

  const email = document
      .getElementById("register-email")
      .value
      .trim();

  const password = document
      .getElementById("register-password")
      .value;

  if (!name || !email || !password) {
    toast("Preencha todos os campos.", "error");
    return;
  }

  try {
    console.log("Criando usuário:", {
      name,
      email,
    });

    const data = await Api.post(
        CONFIG.ENDPOINTS.register,
        {
          name: name,
          email: email,
          password: password,
        }
    );

    console.log("Resposta do cadastro:", data);

    toast(
        "Conta criada com sucesso! Agora faça login.",
        "success"
    );

    document.getElementById("form-register").reset();

    closeModal("modal-register");

    openModal("modal-login");

  } catch (err) {
    console.error("Erro no cadastro:", err);

    toast(
        err.message || "Não foi possível criar a conta.",
        "error"
    );
  }
}


// LOGOUT
function logout() {
  Session.clear();

  Cart.clear();

  refreshAuthUI();

  showView("catalog");

  toast(
      "Sessão encerrada.",
      "info"
  );
}



function refreshAuthUI() {
  const session = Session.get();

  const logged = !!session;

  const authGuest =
      document.getElementById("auth-guest");

  const authUser =
      document.getElementById("auth-user");

  const navMyProducts =
      document.getElementById("nav-my-products");

  authGuest.classList.toggle(
      "hidden",
      logged
  );

  authUser.classList.toggle(
      "hidden",
      !logged
  );

  navMyProducts.classList.toggle(
      "hidden",
      !logged
  );

  if (logged) {
    document.getElementById(
        "session-user"
    ).textContent =
        session.user || session.email;
  }
}




async function loadProducts() {
  const grid =
      document.getElementById("catalog-grid");

  grid.innerHTML =
      `<p class="hint">Carregando produtos da banca...</p>`;

  try {
    const data = await Api.get(
        CONFIG.ENDPOINTS.products
    );

    allProducts =
        normalizeProducts(data);

    renderCatalog(allProducts);

  } catch (err) {

    console.error(err);

    grid.innerHTML =
        `<p class="hint error">
        Não deu pra carregar os produtos: ${err.message}
      </p>`;
  }
}


function normalizeProducts(data) {

  return (Array.isArray(data) ? data : [])
      .map((p) => ({

        idProduct:
            p.idProduct ??
            p.id_product ??
            p.id,

        name:
        p.name,

        value:
            Number(p.value),

        quantity:
            Number(p.quantity),

        idUser:
            p.idUser ??
            p.id_user,

      }));
}


function renderCatalog(products) {

  const grid =
      document.getElementById("catalog-grid");

  if (!products.length) {

    grid.innerHTML =
        `<p class="hint">
        Nenhum produto na banca ainda.
      </p>`;

    return;
  }

  grid.innerHTML =
      products
          .map((p) => {

            const outOfStock =
                p.quantity <= 0;

            return `
          <article class="ticket">

            <div class="ticket-notch"></div>

            <h3 class="ticket-name">
              ${escapeHtml(p.name)}
            </h3>

            <div class="ticket-row">

              <span class="ticket-price">
                ${money(p.value)}
              </span>

              <span class="stamp ${
                outOfStock
                    ? "stamp-out"
                    : ""
            }">

                ${
                outOfStock
                    ? "esgotado"
                    : `${p.quantity} disp.`
            }

              </span>

            </div>

            <button
              class="btn btn-add"
              ${
                outOfStock
                    ? "disabled"
                    : ""
            }

              onclick='Cart.add(${JSON.stringify(
                p
            )})'
            >
              Adicionar ao carrinho
            </button>

          </article>
        `;
          })
          .join("");
}


function filterCatalog(term) {

  const t =
      term.trim().toLowerCase();

  const filtered =
      t
          ? allProducts.filter(
              (p) =>
                  p.name
                      .toLowerCase()
                      .includes(t)
          )
          : allProducts;

  renderCatalog(filtered);
}


// ---------------------------------------------------------------------
// CARRINHO
// ---------------------------------------------------------------------

function render() {

  const list =
      document.getElementById("cart-list");

  const countBadge =
      document.getElementById("cart-count");

  countBadge.textContent =
      Cart.totalItems();

  if (!Cart.items.length) {

    list.innerHTML =
        `<p class="hint">
        Seu carrinho está vazio.
      </p>`;

  } else {

    list.innerHTML =
        Cart.items
            .map(
                (i) => `

          <div class="cart-row">

            <div class="cart-row-main">

              <span class="cart-row-name">
                ${escapeHtml(i.name)}
              </span>

              <span class="cart-row-unit">
                ${money(i.unitValue)} / un.
              </span>

            </div>

            <div class="cart-row-qty">

              <button
                onclick="Cart.updateQty(
                  ${i.idProduct},
                  ${i.quantity - 1}
                )"
              >
                −
              </button>

              <span>
                ${i.quantity}
              </span>

              <button
                onclick="Cart.updateQty(
                  ${i.idProduct},
                  ${i.quantity + 1}
                )"
              >
                +
              </button>

            </div>

            <span class="cart-row-sub">
              ${money(
                    i.unitValue *
                    i.quantity
                )}
            </span>

          </div>
        `
            )
            .join("");
  }

  document.getElementById(
      "cart-total"
  ).textContent =
      money(Cart.totalValue());
}


function toggleCart(open) {

  document
      .getElementById("cart-drawer")
      .classList.toggle(
      "open",
      open
  );

  document
      .getElementById("overlay")
      .classList.toggle(
      "show",
      open
  );
}


// CHECKOUT
async function checkout() {

  const session =
      Session.get();

  if (!session) {

    toast(
        "Faça login para fechar o pedido.",
        "error"
    );

    openModal("modal-login");

    return;
  }

  if (!Cart.items.length) {

    toast(
        "Seu carrinho está vazio.",
        "error"
    );

    return;
  }

  const payload = {

    idUser:
    session.idUser,

    totalValue:
        Cart.totalValue(),

    itemQuantity:
        Cart.totalItems(),

    items:
        Cart.items.map(
            (i) => ({

              idProduct:
              i.idProduct,

              quantity:
              i.quantity,

              unitValue:
              i.unitValue,

            })
        ),

  };

  try {

    await Api.post(
        CONFIG.ENDPOINTS.carts,
        payload
    );

    toast(
        "Pedido fechado com sucesso!",
        "success"
    );

    Cart.clear();

    toggleCart(false);

    loadProducts();

  } catch (err) {

    toast(
        `Não deu pra fechar o pedido: ${err.message}`,
        "error"
    );
  }
}


// ---------------------------------------------------------------------
// MEUS PRODUTOS
// ---------------------------------------------------------------------

async function loadMyProducts() {

  const session =
      Session.get();

  const list =
      document.getElementById(
          "my-products-list"
      );

  if (!session) return;

  try {

    const data =
        await Api.get(
            CONFIG.ENDPOINTS.productsByUser(
                session.idUser
            )
        );

    const mine =
        normalizeProducts(data);

    renderMyProducts(mine);

  } catch (err) {

    list.innerHTML =
        `<p class="hint error">
        ${err.message}
      </p>`;
  }
}


function renderMyProducts(products) {

  const list =
      document.getElementById(
          "my-products-list"
      );

  if (!products.length) {

    list.innerHTML =
        `<p class="hint">
        Você ainda não cadastrou produtos.
      </p>`;

    return;
  }

  list.innerHTML =
      products
          .map(
              (p) => `

        <div class="my-row">

          <div>

            <strong>
              ${escapeHtml(p.name)}
            </strong>

            <span class="hint">
              ${money(p.value)}
              ·
              ${p.quantity} un.
            </span>

          </div>

          <div class="my-row-actions">

            <button
              class="btn-icon"
              title="Editar"
              onclick="openEditProduct(
                ${p.idProduct}
              )"
            >
              ✎
            </button>

            <button
              class="btn-icon"
              title="Remover"
              onclick="deleteProduct(
                ${p.idProduct}
              )"
            >
              🗑
            </button>

          </div>

        </div>
      `
          )
          .join("");
}


// FORMULÁRIO DE PRODUTO
async function handleProductForm(e) {

  e.preventDefault();

  const session =
      Session.get();

  if (!session) {

    toast(
        "Faça login primeiro.",
        "error"
    );

    return;
  }

  const idField =
      document.getElementById(
          "product-id"
      );

  const idProduct =
      idField.value
          ? Number(idField.value)
          : null;

  const payload = {

    name:
        document
            .getElementById("product-name")
            .value
            .trim(),

    value:
        Number(
            document
                .getElementById(
                    "product-value"
                )
                .value
        ),

    quantity:
        Number(
            document
                .getElementById(
                    "product-quantity"
                )
                .value
        ),

    idUser:
    session.idUser,
  };

  try {

    if (idProduct) {

      await Api.put(
          CONFIG.ENDPOINTS.productById(
              idProduct
          ),
          payload
      );

      toast(
          "Produto atualizado.",
          "success"
      );

    } else {

      await Api.post(
          CONFIG.ENDPOINTS.products,
          payload
      );

      toast(
          "Produto cadastrado.",
          "success"
      );
    }

    e.target.reset();

    idField.value = "";

    loadMyProducts();

    loadProducts();

  } catch (err) {

    toast(
        err.message,
        "error"
    );
  }
}


function openEditProduct(idProduct) {

  const p =
      allProducts.find(
          (x) =>
              x.idProduct === idProduct
      );

  if (!p) return;

  document.getElementById(
      "product-id"
  ).value =
      p.idProduct;

  document.getElementById(
      "product-name"
  ).value =
      p.name;

  document.getElementById(
      "product-value"
  ).value =
      p.value;

  document.getElementById(
      "product-quantity"
  ).value =
      p.quantity;

  document.getElementById(
      "product-name"
  ).focus();
}


async function deleteProduct(idProduct) {

  if (
      !confirm(
          "Remover este produto da banca?"
      )
  ) {
    return;
  }

  try {

    await Api.del(
        CONFIG.ENDPOINTS.productById(
            idProduct
        )
    );

    toast(
        "Produto removido.",
        "success"
    );

    loadMyProducts();

    loadProducts();

  } catch (err) {

    toast(
        err.message,
        "error"
    );
  }
}




function openModal(id) {

  const modal =
      document.getElementById(id);

  if (!modal) return;

  modal.classList.add("show");

  document
      .getElementById("overlay")
      .classList.add("show");
}


function closeModal(id) {

  const modal =
      document.getElementById(id);

  if (!modal) return;

  modal.classList.remove("show");

  document
      .getElementById("overlay")
      .classList.remove("show");
}


function closeAllOverlays() {

  document
      .querySelectorAll(
          ".modal.show"
      )
      .forEach(
          (m) =>
              m.classList.remove("show")
      );

  document
      .getElementById(
          "cart-drawer"
      )
      .classList.remove("open");

  document
      .getElementById(
          "overlay"
      )
      .classList.remove("show");
}




function showView(view) {

  document
      .getElementById(
          "view-catalog"
      )
      .classList.toggle(
      "hidden",
      view !== "catalog"
  );

  document
      .getElementById(
          "view-my-products"
      )
      .classList.toggle(
      "hidden",
      view !== "my-products"
  );

  document
      .querySelectorAll(
          ".nav-link"
      )
      .forEach(
          (a) =>
              a.classList.toggle(
                  "active",
                  a.dataset.view === view
              )
      );

  if (
      view === "my-products"
  ) {
    loadMyProducts();
  }
}


// ---------------------------------------------------------------------
// SEGURANÇA HTML
// ---------------------------------------------------------------------

function escapeHtml(str) {

  return String(str)
      .replace(
          /&/g,
          "&amp;"
      )
      .replace(
          /</g,
          "&lt;"
      )
      .replace(
          />/g,
          "&gt;"
      )
      .replace(
          /"/g,
          "&quot;"
      );
}


// ---------------------------------------------------------------------
// INICIALIZAÇÃO
// ---------------------------------------------------------------------

document.addEventListener(
    "DOMContentLoaded",
    () => {

      console.log(
          "Market: app.js carregado!"
      );

      refreshAuthUI();

      loadProducts();

      // LOGIN
      const formLogin =
          document.getElementById(
              "form-login"
          );

      if (formLogin) {

        formLogin.addEventListener(
            "submit",
            handleLogin
        );
      }

      // CADASTRO
      const formRegister =
          document.getElementById(
              "form-register"
          );

      if (formRegister) {

        formRegister.addEventListener(
            "submit",
            handleRegister
        );
      }

      // PRODUTO
      const formProduct =
          document.getElementById(
              "form-product"
          );

      if (formProduct) {

        formProduct.addEventListener(
            "submit",
            handleProductForm
        );
      }

      // PESQUISA
      const searchInput =
          document.getElementById(
              "search-input"
          );

      if (searchInput) {

        searchInput.addEventListener(
            "input",
            (e) =>
                filterCatalog(
                    e.target.value
                )
        );
      }

      // OVERLAY
      const overlay =
          document.getElementById(
              "overlay"
          );

      if (overlay) {

        overlay.addEventListener(
            "click",
            closeAllOverlays
        );
      }

      // SESSÃO
      if (
          typeof Session !== "undefined" &&
          Session.isLogged()
      ) {

        loadMyProducts();
      }

      render();
    }
);