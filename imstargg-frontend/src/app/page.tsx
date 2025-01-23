import Image from "next/image";
import BrawlStarsNewsList from "@/components/brawl-stars-news-list";
import React from "react";
import PlayerSearchForm from "@/components/player-search-form";

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center my-4 p-2">
      <div className="mb-8">
        <Image
          src="/zBrawl Stars Logo 2_starr_parkk.png"
          alt="ImStarGG 로고"
          width={200}
          height={100}
          priority
        />
      </div>
      
      <PlayerSearchForm/>

      <BrawlStarsNewsList />
    </div>
  );
}
