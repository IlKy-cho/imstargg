import {Brawler} from "@/model/Brawler";
import {ReactNode} from "react";
import {brawlerHref} from "@/config/docs";
import Link from "next/link";

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

type BrawlerProfileProps = { brawler: Brawler };

export function BrawlerProfile({brawler}: Readonly<BrawlerProfileProps>) {
  return (
    <div className="flex gap-2 p-6 rounded-lg shadow-md border bg-zinc-100 bg-opacity-90 m-2 max-w-screen-sm">
    </div>
  );
}

