import Image from "next/image";
import React from "react";
import PlayerSearchForm from "@/components/player-search-form";
import {getBrawlStarsNews} from "@/lib/api/brawlstars";
import {BrawlStarsNewsList} from "@/components/brawl-stars-news";

export default async function Home() {

  const newsList = await  getBrawlStarsNews();

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

      <div className="w-full max-w-xl m-1">
        <PlayerSearchForm/>
      </div>

      <BrawlStarsNewsList newsList={newsList}/>
    </div>
  );
}
