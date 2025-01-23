import type {Metadata} from "next";
import localFont from "next/font/local";
import "./globals.css";
import React from "react";
import SiteHeader from "@/components/site-header";
import Footer from "@/components/footer";
import SideArea from "@/components/side-area";
import {meta} from "@/config/site";
import { GoogleAnalytics } from "@/lib/third-parties";

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
  title: {
    template: `%s | ${meta.name}`,
    default: meta.name,
    absolute: meta.name,
  },
  description: "브롤스타즈 전적 검색부터 데이터 분석, 메타 분석까지 다양한 정보를 제공합니다.",
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
      <GoogleAnalytics/>
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