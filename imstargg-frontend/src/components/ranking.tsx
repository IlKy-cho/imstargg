import {PlayerRanking as PlayerRankingModel} from "@/model/PlayerRanking";
import {Country} from "@/model/enums/Country";
import {CountrySelect} from "@/components/ranking-option";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import {ScrollArea} from "@/components/ui/scroll-area";
import {Separator} from "@/components/ui/separator";
import {cva, VariantProps} from "class-variance-authority";
import {cn} from "@/lib/utils";
import Link from "next/link";
import {playerHref} from "@/config/site";

interface RankingProps extends RankingListProps, RankingTitleProps {}

export async function Ranking({rankings, country, size}: Readonly<RankingProps>) {
  return (
    <div className="flex flex-col p-2 gap-2 border border-zinc-200 rounded">
      <RankingTitle country={country}/>
      <Separator/>
      <RankingList rankings={rankings} size={size}/>
    </div>
  )
}

const listVariants = cva(
  "",
  {
    variants: {
      size: {
        default: 'h-80 sm:h-96',
        screen: 'h-dvh'
      },
    },
    defaultVariants: {
      size: 'default',
    },
  },
);

interface RankingListProps extends VariantProps<typeof listVariants> {
  rankings: PlayerRankingModel[];
}

export async function RankingList({rankings, size}: Readonly<RankingListProps>) {

  return (
    <ScrollArea className={cn(listVariants({size}))}>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead className="text-xs sm:text-sm">순위</TableHead>
            <TableHead className="text-xs sm:text-sm">플레이어</TableHead>
            <TableHead className="text-xs sm:text-sm">클럽</TableHead>
            <TableHead className="text-xs sm:text-sm">트로피</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {rankings.map((ranking) => (
            <TableRow key={ranking.tag}>
              <TableCell className="text-xs sm:text-sm">
                {ranking.rank}
              </TableCell>
              <TableCell className="text-xs sm:text-sm">
                <Link href={playerHref(ranking.tag)}>
                  {ranking.name} <span className="hidden sm:inline text-zinc-500">{ranking.tag}</span>
                </Link>
              </TableCell>
              <TableCell className="text-xs sm:text-sm">
                {ranking.clubName}
              </TableCell>
              <TableCell className="text-xs sm:text-sm">
                {ranking.trophies === 1 ? "100000+" : ranking.trophies}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </ScrollArea>
  );
}

interface RankingTitleProps {
  country: Country;
}

export async function RankingTitle({country}: Readonly<RankingTitleProps>) {
  return (
    <div className="flex justify-between">
      <h2 className="text-xl sm:text-2xl font-bold">
        랭킹
      </h2>
      <CountrySelect country={country}/>
    </div>
  );
}