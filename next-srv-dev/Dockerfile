# Etapa base
FROM node:20-alpine AS base

# Instalar dependencias solo cuando sea necesario
FROM base AS deps
RUN apk add --no-cache libc6-compat python3 make g++
WORKDIR /app

# Copiar y habilitar pnpm
COPY pnpm-lock.yaml ./
COPY package.json ./
RUN corepack enable pnpm && pnpm i --frozen-lockfile

# Recompilar las bibliotecas nativas
RUN pnpm rebuild bcrypt

# Construir el código fuente solo cuando sea necesario
FROM base AS builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .

# Habilitar pnpm en la etapa de construcción
RUN corepack enable pnpm

# Deshabilitar la telemetría de Next.js si es necesario
ENV NEXT_TELEMETRY_DISABLED 1

# Construir el proyecto
RUN pnpm run build

RUN ls -la /app/.next
# Etapa de producción
FROM node:20-alpine AS runner
WORKDIR /app
RUN npm install -g pnpm
# Si usas la telemetría de Next.js, puedes deshabilitarla en producción
ENV NEXT_TELEMETRY_DISABLED 1

# Copiar los archivos necesarios para la producción
COPY --from=builder /app/next.config.mjs ./
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json

# Configurar la variable de entorno para producción
ENV NODE_ENV production

# Exponer el puerto que utiliza Next.js
EXPOSE 3000

# Comando para iniciar la aplicación
CMD ["pnpm", "start"]