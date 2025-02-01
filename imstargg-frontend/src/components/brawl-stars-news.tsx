import type {BrawlStarsNews as BrawlStarsNewsModel} from "@/model/brawlstars/BrawlStarsNews";
import Link from "next/link";
import dayjs from "dayjs";

type NewsListProps = {
  newsList: BrawlStarsNewsModel[];
}

export function BrawlStarsNewsList({newsList}: Readonly<NewsListProps>) {


  return (
    <div className="flex flex-col gap-4 max-w-2xl w-full my-5">
      <Link href='https://supercell.com/en/games/brawlstars/ko/blog/' target="_blank">
        <h1 className="text-xl sm:text-2xl font-bold">브롤스타즈 뉴스</h1>
      </Link>


      <div className="flex flex-col gap-1">
        {
          newsList.map((news) => (
            <BrawlStarsNews key={news.linkUrl} news={news}/>
          ))
        }
      </div>
    </div>
  );
}

type NewsProps = {
  news: BrawlStarsNewsModel;
}


function BrawlStarsNews({news}: NewsProps) {
  return (
    <Link href={news.linkUrl} target="_blank">
      <div className="p-3 sm:p-4 border rounded-lg hover:bg-zinc-100">
        <div className="flex items-center justify-between gap-2">
          <h2 className="text-base sm:text-lg font-semibold">{news.title}</h2>
          <div className="text-xs sm:text-sm text-zinc-600">
            {dayjs(news.publishDate).format('YYYY.MM.DD')}
          </div>
        </div>
      </div>
    </Link>
  );
}
