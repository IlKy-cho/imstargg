import type { Metadata } from "next";
import localFont from "next/font/local";
import "./globals.css";
import Footer from "./_component/Footer";
import NavBar from "./_component/NavBar";
import SideAd from './_component/SideAd';
import BottomAd from './_component/BottomAd';

const geistSans = localFont({
  src: "./fonts/GeistVF.woff",
  variable: "--font-geist-sans",
  weight: "100 900",
});
const geistMono = localFont({
  src: "./fonts/GeistMonoVF.woff",
  variable: "--font-geist-mono",
  weight: "100 900",
});

export const metadata: Metadata = {
  title: "ImStarGG",
  description: "브롤스타즈 통계",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body className={`${geistSans.variable} ${geistMono.variable} antialiased`}>
        <div className="flex flex-col min-h-screen">
          <NavBar />
          <div className="flex flex-1">
            <SideAd />
            
            <div className="flex-1">
              <main className="flex-grow pb-[100px]">
                {children}
              </main>
            </div>

            <SideAd />
          </div>
          <Footer />
          <BottomAd />
        </div>
      </body>
    </html>
  );
}