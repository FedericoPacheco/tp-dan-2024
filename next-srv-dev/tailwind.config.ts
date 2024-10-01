import type { Config } from 'tailwindcss';

const config: Config = {
  content: [
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      gridTemplateColumns: {
        '13': 'repeat(13, minmax(0, 1fr))',
      },
      colors: {
        blue: {
          400: '#2589FE',
          500: '#0070F3',
          600: '#2F6FEB',
        },
        ebony: {
          DEFAULT: '#2F2F2F',
          100: '#4B4B4B',
          200: '#454545',
          300: '#3F3F3F',
          400: '#393939',
          500: '#2F2F2F',
          600: '#292929',
          700: '#232323',
          800: '#1D1D1D',
          900: '#171717',
        },
        charcoal: '#1C1C1C',
        graphite: '#383838',
        //Para fuente de texto : 
        white:{
          Default: '#FFFFFF',
          50: '#F9F9F9',      // Blanco muy claro
          100: '#F5F5F5',     // Blanco humo
          200: '#F0F0F0',     // Blanco hueso
          300: '#EBEBEB',     // Blanco ceniza
          400: '#E0E0E0',     // Gris claro
          500: '#DCDCDC',     // Gris perla
          600: '#C0C0C0',     // Gris niebla
          700: '#BEBEBE',     // Gris claro 2
          800: '#B0B0B0',     // Gris más oscuro
          900: '#A0A0A0', 
        },
        lightGray: '#E0E0E0',     // Gris claro
        softYellow: '#FFEB3B',    // Amarillo suave
        gold: '#FFD700',          // Oro
        pastelBlue: '#A3D8F4',    // Azul pastel
        pastelGreen: '#A8E6CF',   // Verde pastel
        silver: '#C0C0C0',        // Plateado claro
        beige: '#F5F5DC',         // Beige claro
        palePink: '#FFC0CB',      // Rosa pálido
        lightBlue: '#DDEEFF',     // Azul claro
        slateBlue: '#B0C4DE',     // Azul pizarra
        brightRed: '#FF4136',     // Rojo brillante
        cyan: '#00FFFF',          // Cian claro
      },
    },
    keyframes: {
      shimmer: {
        '100%': {
          transform: 'translateX(100%)',
        },
      },
    },
  },
  plugins: [require('@tailwindcss/forms')],
};
export default config;
