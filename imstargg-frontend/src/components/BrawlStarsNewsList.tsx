"use client";

import React, {useEffect, useState} from "react";
import {BrawlStarsNews} from "@/model/brawlstars/BrawlStarsNews";
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationNext,
  PaginationPrevious
} from "@/components/ui/pagination";
import BrawlStarsNewsComponent from "@/components/BrawlStarsNews";
import {getBrawlStarsNewsPage} from "@/lib/api/getBrawlStarsNewsPage";

export default function BrawlStarsNewsList() {
  const [page, setPage] = useState<number>(1);
  const [loading, setLoading] = useState(true);
  const [hasNext, setHasNext] = useState<boolean>(false);
  const [brawlStarsNewsList, setBrawlStarsNewsList] = useState<BrawlStarsNews[]>([]);

  useEffect(() => {
    const fetchNews = async () => {
      setLoading(true);
      try {
        const response = await getBrawlStarsNewsPage(page);
        setBrawlStarsNewsList(response.content);
        setHasNext(response.hasNext);
      } catch (error) {
        console.error('뉴스를 불러오는데 실패했습니다:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchNews();
  }, [page]);

  return (
    <div className="space-y-4 max-w-2xl mx-auto my-5 w-full">
      <h1 className="text-2xl font-bold">브롤스타즈 뉴스</h1>

      <div className="flex flex-col gap-1">
        {loading
          ? Array(6).fill(null).map((_, index) => (
            <BrawlStarsNewsComponent key={`skeleton-${index}`} news={null}/>
          ))
          : brawlStarsNewsList.map((news) => (
            <BrawlStarsNewsComponent key={news.linkUrl} news={news}/>
          ))
        }
      </div>

      <Pagination>
        <PaginationContent>
          <PaginationItem>
            <PaginationPrevious
              onClick={() => setPage(prev => Math.max(1, prev - 1))}
              className={`cursor-pointer ${page <= 1 ? 'pointer-events-none opacity-50' : ''}`}
            />
          </PaginationItem>
          <PaginationItem>
            {page}
          </PaginationItem>
          <PaginationItem>
            <PaginationNext
              onClick={() => hasNext && setPage(prev => prev + 1)}
              className={`cursor-pointer ${!hasNext ? 'pointer-events-none opacity-50' : ''}`}
            />
          </PaginationItem>
        </PaginationContent>
      </Pagination>
    </div>
  );
}