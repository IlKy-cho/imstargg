import type {Metadata} from "next";
import localFont from "next/font/local";
import "./globals.css";
import React from "react";
import SiteHeader from "@/components/site-header";
import Footer from "@/components/footer";
import SideArea from "@/components/side-area";
import { GoogleAnalytics } from '@next/third-parties/google'
import {meta} from "@/config/site";

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
  title: `${meta.name} | 브롤스타즈 데이터 분석 전적 검색 아임스타지지`,
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
      {
        process.env.NEXT_PUBLIC_GOOGLE_ANALYTICS && (
          <GoogleAnalytics gaId={process.env.NEXT_PUBLIC_GOOGLE_ANALYTICS}/>
        )
      }
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