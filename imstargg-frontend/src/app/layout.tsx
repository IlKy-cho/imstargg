import type {Metadata} from "next";
import localFont from "next/font/local";
import "./globals.css";
import React from "react";
import SiteHeader from "@/components/site-header";
import Footer from "@/components/footer";
import SideArea from "@/components/side-area";

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
  description: "브롤스타즈 전적 검색. 브롤스타즈 브롤러. 브롤스타즈 이벤트. 브롤스타즈 통계.",
};

export default function RootLayout({
                                     children,
                                   }: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
    <body
      className={`${geistSans.variable} ${geistMono.variable} antialiased`}
    >
    <div className="flex flex-col min-h-screen">
      <SiteHeader/>
      <div className="flex flex-1">
        <SideArea/>

        <main className="flex-1 max-w-screen-lg mx-auto">
          {children}
        </main>

        <SideArea/>
      </div>
      <Footer/>
    </div>
    </body>
    </html>
  );
}