"use client";

import type {BrawlStarsNews} from "@/model/brawlstars/BrawlStarsNews";
import Link from "next/link";
import dayjs from "dayjs";
import {Skeleton} from "@/components/ui/skeleton";


type Props = {
  news: BrawlStarsNews | null;
}

const NewsContainer = ({ news } : Props) => {
  return (
    <div className="p-4 border rounded-lg cursor-pointer hover:bg-zinc-100">
      <div className="flex items-center gap-2">
        {news ?
          <h2 className="text-lg font-semibold">{news.title}</h2>
          : <Skeleton className="h-6 w-[300px] my-1"/>
        }
      </div>
      <div className="text-sm text-zinc-600">
        {news ?
          dayjs(news.publishDate).format('YYYY.MM.DD')
          : <Skeleton className="h-3 w-[80px]"/>
        }
      </div>
    </div>
  );
};

export default function BrawlStarsNews({news}: Props) {
  return (
    news ?
      <Link href={news.linkUrl}>
        <NewsContainer news={news}/>
      </Link>
      :
      <NewsContainer news={news}/>
  );
}