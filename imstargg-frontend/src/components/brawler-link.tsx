import { Brawler } from "@/model/Brawler";
import Link from "next/link";
import React, { ReactNode } from "react";
import {brawlerHref} from "@/config/site";

type BrawlerLinkProps = {
  brawler: Brawler | null;
  children?: ReactNode;
}

export function BrawlerLink({ brawler, children }: Readonly<BrawlerLinkProps>) {
  if (!brawler) {
    return children;
  }

  return (
    <Link href={brawlerHref(brawler)}>
      {children}
    </Link>
  );
}