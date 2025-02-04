import type {BrawlStarsNews as BrawlStarsNewsModel} from "@/model/brawlstars/BrawlStarsNews";
import Link from "next/link";
import {NewsChannelItem, newsChannelItems} from "@/config/docs";
import Image from "next/image";

export function BrawlStarsNewsChannel() {


  return (
    <div className="flex flex-col gap-4">
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

type NewsProps = {
  news: BrawlStarsNewsModel;
}


function BrawlStarsNewsChannelItem({item}: Readonly<{item: NewsChannelItem}>) {
  return (
    <Link href={item.href} target="_blank">
      <Image src={item.icon} alt={item.label} className="w-10 h-10" />
    </Link>
  );
}
