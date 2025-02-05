import Image from "next/image";
import React from "react";
import PlayerSearchForm from "@/components/player-search-form";
import {NewsChannelItem, newsChannelItems} from "@/config/docs";
import Link from "next/link";


export default async function Home() {

  return (
    <div className="flex flex-col items-center justify-center my-4 p-8">
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

      <div className="mt-8">
        <BrawlStarsNewsChannel/>
      </div>

    </div>
  );
}

function BrawlStarsNewsChannel() {
  return (
    <div className="flex flex-col gap-4 items-center">
      <h1 className="text-xl sm:text-2xl font-bold">브롤스타즈 소식 채널</h1>

      <div className="flex gap-1">
        {
          newsChannelItems.map((item) => (
            <BrawlStarsNewsChannelItem key={item.label} item={item} />
          ))
        }
      </div>
    </div>
  );
}

function BrawlStarsNewsChannelItem({item}: Readonly<{item: NewsChannelItem}>) {
  return (
    <Link href={item.href} target="_blank">
      <Image src={item.icon} alt={item.label} className="w-10 h-10" />
    </Link>
  );
}