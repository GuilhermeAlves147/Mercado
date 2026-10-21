/**
 * Configuração do frontend.
 *
 * - Servido pelo próprio Spring Boot (http://localhost:PORTA/):  API_BASE = "" (mesma origem).
 * - Aberto direto do arquivo (file://) ou via Live Server:        usa BACKEND_URL abaixo.
 *
 * Se o backend rodar em outra porta, altere BACKEND_URL.
 */
const BACKEND_URL = "http://localhost:8080";

const CONFIG = {
  API_BASE: (() => {
    if (window.MERCADO_API_BASE !== undefined) return window.MERCADO_API_BASE; // override manual
    const { protocol, port } = window.location;
    const servidoPeloSpring = protocol.startsWith("http") && !["5500", "5501", "5173", "3000", "8000"].includes(port);
    return servidoPeloSpring ? "" : BACKEND_URL;
  })(),
};
