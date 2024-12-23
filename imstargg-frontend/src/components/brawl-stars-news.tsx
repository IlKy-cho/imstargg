"use client";

import type {BrawlStarsNews} from "@/model/brawlstars/BrawlStarsNews";
import {Card, CardContent} from "@/components/ui/card";
import Link from "next/link";
import dayjs from "dayjs";
import {Skeleton} from "@/components/ui/skeleton";


type Props = {
  news: BrawlStarsNews | null;
}

export default function BrawlStarsNews({ news }: Props) {
  return (
    <Card className="transition-colors hover:bg-zinc-100 dark:hover:bg-zinc-800">
      {news ?
        <Link href={news.linkUrl}>
          <CardContent>
            <h2 className="text-lg font-semibold">{news.title}</h2>
            <div className="text-sm text-zinc-500">
              {dayjs(news.publishDate).format('YYYY.MM.DD')}
            </div>
          </CardContent>
        </Link>
        :
        <CardContent>
          <Skeleton className="h-6 w-[300px] my-1" />
          <div>
            <Skeleton className="h-3 w-[80px]" />
          </div>
        </CardContent>
      }
    </Card>
  );
}