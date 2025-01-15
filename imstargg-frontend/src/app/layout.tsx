import type {Metadata} from "next";
import localFont from "next/font/local";
import "./globals.css";
import React from "react";
import SiteHeader from "@/components/site-header";
import Footer from "@/components/footer";
import SideArea from "@/components/side-area";

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