import {Brawler} from "@/model/Brawler";
import {ReactNode} from "react";
import Link from "next/link";
import {brawlerHref} from "@/config/docs";

type Props = {
  brawler: Brawler | null;
  className?: string;
  children?: ReactNode;
}

export default function BrawlerLink({ brawler, className, children }: Readonly<Props>) {
  if (!brawler) {
    return {children};
  }

  return (
    <Link href={brawlerHref(brawler)} className={className}>
      {children}
    </Link>
  );
}