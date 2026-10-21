const { test, expect } = require('@playwright/test');

// Conta única por execução: o teste cria um usuário novo e não depende de senhas da carga.
const ID = Date.now();
const EMAIL = `e2e_${ID}@teste.com`;
const SENHA = 'senha123';
const NOME = `e2e${ID % 1000000}`;

async function criarConta(page) {
  await page.locator('#accountBtn').click();
  await page.getByRole('tab', { name: 'Criar conta' }).click();
  await page.locator('#f-user').fill(NOME);
  await page.locator('#f-email').fill(EMAIL);
  await page.locator('#f-pass').fill(SENHA);
  await page.getByRole('button', { name: 'Criar conta e entrar' }).click();
}

async function entrar(page, email = EMAIL, senha = SENHA) {
  await page.locator('#accountBtn').click();
  await page.locator('#f-email').fill(email);
  await page.locator('#f-pass').fill(senha);
  await page.getByRole('dialog').getByRole('button', { name: 'Entrar', exact: true }).click();
}

// Cada teste abre um navegador novo (sem sessão): este helper entra com a conta criada no E2E-05.
async function abrirLogado(page, hash = '/') {
  await page.goto(hash);
  await entrar(page);
  await expect(page.locator('#accountBtn')).toContainText(NOME);
}

test.describe.configure({ mode: 'serial' });

test('E2E-01: catálogo carrega os produtos vindos da API', async ({ page }) => {
  await page.goto('/');
  await expect(page.locator('.card').first()).toBeVisible();
  expect(await page.locator('.card').count()).toBeGreaterThan(0);
  await expect(page.locator('.count')).toContainText('produto');
});

test('E2E-02: busca filtra a lista de produtos', async ({ page }) => {
  await page.goto('/');
  await expect(page.locator('.card').first()).toBeVisible();
  const total = await page.locator('.card').count();
  await page.locator('#search').fill('zzzprodutoinexistente');
  await expect(page.getByText('Nada por aqui')).toBeVisible();
  await page.locator('#search').fill('');
  await expect(page.locator('.card')).toHaveCount(total);
});

test('E2E-03: adicionar ao carrinho sem login pede para entrar', async ({ page }) => {
  await page.goto('/');
  await page.locator('[data-action="add"]:not([disabled])').first().click();
  await expect(page.getByRole('dialog')).toContainText('Entre na sua conta');
});

test('E2E-04: login com credenciais inválidas mostra erro', async ({ page }) => {
  await page.goto('/');
  await entrar(page, 'naoexiste@teste.com', 'errada123');
  await expect(page.locator('.form-error')).toContainText('E-mail ou senha inválidos');
});

test('E2E-05: cadastro de usuário já deixa a pessoa logada', async ({ page }) => {
  await page.goto('/');
  await criarConta(page);
  await expect(page.locator('#accountBtn')).toContainText(NOME);
});

test('E2E-06: login com a conta criada e logout', async ({ page }) => {
  await abrirLogado(page);
  await page.locator('#accountBtn').click();
  await page.getByRole('button', { name: 'Sair da conta' }).click();
  await expect(page.locator('#accountBtn')).toHaveText('Entrar');
});

test('E2E-07: compra - adicionar ao carrinho e finalizar o pedido', async ({ page }) => {
  await abrirLogado(page);
  await page.locator('[data-action="add"]:not([disabled])').first().click();
  await expect(page.locator('#cartCount')).toHaveText('1');
  await page.locator('#cartBtn').click();
  await expect(page.locator('#drawer')).toContainText('Total');
  await page.getByRole('button', { name: 'Finalizar pedido' }).click();
  await expect(page).toHaveURL(/#\/pedidos/);
  await expect(page.locator('.order').first()).toContainText('Aguardando pagamento');
  await expect(page.locator('#cartCount')).toBeHidden();
});

test('E2E-08: pagar o pedido muda o status para Pago', async ({ page }) => {
  await abrirLogado(page, '/#/pedidos');
  await page.getByRole('button', { name: 'Pagar agora' }).first().click();
  await expect(page.locator('.order').first()).toContainText('Pago');
});

test('E2E-09: cancelar um pedido novo (aberto) devolve o status Cancelado', async ({ page }) => {
  await abrirLogado(page);
  await page.locator('[data-action="add"]:not([disabled])').first().click();
  await expect(page.locator('#cartCount')).toHaveText('1');
  await page.locator('#cartBtn').click();
  await page.getByRole('button', { name: 'Finalizar pedido' }).click();
  await expect(page).toHaveURL(/#\/pedidos/);
  await page.locator('.order').first().getByRole('button', { name: 'Cancelar pedido' }).click();
  await page.getByRole('dialog').getByRole('button', { name: 'Cancelar pedido' }).click();
  await expect(page.locator('.order').first()).toContainText('Cancelado');
});
