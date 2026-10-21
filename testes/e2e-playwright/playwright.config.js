const { defineConfig } = require('@playwright/test');

// URL do sistema em teste. Padrão: Mercado rodando no IntelliJ com SERVER_PORT=8081.
// Para outra porta:  set BASE_URL=http://localhost:8080  (Windows CMD)
const baseURL = process.env.BASE_URL || 'http://localhost:8081';

module.exports = defineConfig({
  testDir: './tests',
  timeout: 30000,
  workers: 1,                 // testes em sequência (todos usam o mesmo banco)
  retries: 0,
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    baseURL,
    headless: process.env.HEADED ? false : true,
    screenshot: 'on',          // um print por teste = evidência para o relatório
    trace: 'retain-on-failure',
    viewport: { width: 1280, height: 800 },
    launchOptions: process.env.CHROMIUM_PATH ? { executablePath: process.env.CHROMIUM_PATH } : {},
  },
});
