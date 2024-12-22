import type { Metadata } from "next";
import localFont from "next/font/local";
import "./globals.css";
import Footer from "./_component/Footer";
import SideAd from './_component/SideAd';
import BottomAd from './_component/BottomAd';
import React from "react";
import SiteHeader from "@/components/site-header";

const kobrawl40 = localFont({
  src: "./fonts/KoBrawl Gothic40.otf",
  variable: "--font-kobrawl-40",
});

const kobrawl60 = localFont({
  src: "./fonts/KoBrawl Gothic60.otf",
  variable: "--font-kobrawl-60",
});

const lilitaOne = localFont({
  src: "./fonts/lilitaone-regular.ttf",
  variable: "--font-lilita-one",
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
      <body className={`${kobrawl40.variable} ${kobrawl60.variable} ${lilitaOne.variable} antialiased`}>
        <div className="flex flex-col">
          <SiteHeader />
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