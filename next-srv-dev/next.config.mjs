/** @type {import('next').NextConfig} */

const nextConfig = {
  webpack(config) {
    config.module.rules.push({
      test: /\.svg$/,
      use: ['@svgr/webpack'],
    });

    return config;
  },
  images: {
    dangerouslyAllowSVG: true,
    domains: ['/public/icons/brick.svg'],
  },
  env: {
    AUTH_SECRET: process.env.AUTH_SECRET,
    NEXT_PUBLIC_GW_ADDR: process.env.NEXT_PUBLIC_GW_ADDR,
    POSTGRES_URL : process.env.POSTGRES_URL,
    POSTGRES_PRISMA_URL : process.env.POSTGRES_PRISMA_URL,
    POSTGRES_URL_NO_SSL : process.env.POSTGRES_URL_NO_SSL,
    POSTGRES_URL_NON_POOLING : process.env.POSTGRES_URL_NON_POOLING,
    POSTGRES_USER : process.env.POSTGRES_USER,
    POSTGRES_HOST : process.env.POSTGRES_HOST,
    POSTGRES_PASSWORD : process.env.POSTGRES_PASSWORD,
    POSTGRES_DATABASE : process.env.POSTGRES_DATABASE,
  },
};

export default nextConfig;