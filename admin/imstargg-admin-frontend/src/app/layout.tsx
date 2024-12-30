import type {Metadata} from "next";
import {Geist, Geist_Mono} from "next/font/google";
import "./globals.css";
import SiteHeader from "@/components/site-header";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "아임스타지지 어드민",
  description: "아임스타지지 어드민",
};

export default function RootLayout({children,}: Readonly<{ children: React.ReactNode; }>) {

  return (
    <html lang="ko" className='h-full'>
    <body
      className={`${geistSans.variable} ${geistMono.variable} antialiased h-full`}
    >
    <SiteHeader/>
    <main>
      {children}
    </main>
    </body>
    </html>
  );
}
