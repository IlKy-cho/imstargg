import Image from "next/image";
import BrawlStarsNewsList from "@/components/BrawlStarsNewsList";
import React from "react";
import PlayerSearchForm from "@/components/player-search-form";

export default function Home() {
  return (
    <main className="flex flex-col items-center justify-center my-4">
      <div className="mb-8">
        <Image
          src="/logo.png"
          alt="ImStarGG 로고"
          width={200}
          height={100}
          priority
        />
      </div>
      
      <PlayerSearchForm/>

      <BrawlStarsNewsList />
    </main>
  );
}
