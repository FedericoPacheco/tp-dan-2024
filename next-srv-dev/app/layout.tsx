import '@/app/ui/global.css'
import { inter } from '@/app/ui/fonts/fonts';
import { Metadata } from 'next';

export const metadata: Metadata = {
  title: {
    template: '%s | GestionDAN',
    default: 'Sistemas Gestion DAN',
  },
  description: 'Sitio de gestion de corralones.',
  icons: {
    icon: '/icons/brick.svg',
  },
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className={`${inter.className} antialiased`}>{children}</body>
    </html>
  );
}
