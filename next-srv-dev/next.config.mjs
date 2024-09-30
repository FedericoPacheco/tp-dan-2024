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
};
   
export default nextConfig;