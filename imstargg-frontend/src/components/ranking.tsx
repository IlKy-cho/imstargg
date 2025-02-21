import {PlayerRanking as PlayerRankingModel} from "@/model/PlayerRanking";
import {Country} from "@/model/enums/Country";
import {CountrySelect} from "@/components/ranking-option";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import {ScrollArea} from "@/components/ui/scroll-area";
import {Separator} from "@/components/ui/separator";
import {cva, VariantProps} from "class-variance-authority";
import {cn, cnWithDefault} from "@/lib/utils";
import Link from "next/link";
import {playerHref} from "@/config/site";

interface RankingProps extends RankingListProps, RankingTitleProps {}

export async function Ranking({rankings, country, size}: Readonly<RankingProps>) {
  return (
    <div className={cnWithDefault("flex flex-col gap-2")}>
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
            <TableHead className="text-xs sm:text-sm sm:w-16 w-14">순위</TableHead>
            <TableHead className="text-xs sm:text-sm">플레이어</TableHead>
            <TableHead className="text-xs sm:text-sm w-24 sm:w-36">클럽</TableHead>
            <TableHead className="text-xs sm:text-sm sm:w-16 w-14">트로피</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {rankings.map((ranking) => (
            <TableRow key={ranking.tag}>
              <TableCell className="flex gap-1 text-xs sm:text-sm sm:w-16 w-14">
                <span>
                  {ranking.rank}
                </span>
                <span className={rankChangeColor(ranking.rankChange)}>
                  {rankChangeText(ranking.rankChange)}
                </span>
              </TableCell>
              <TableCell className="text-xs sm:text-sm">
                <Link href={playerHref(ranking.tag)}>
                  {ranking.name} <span className="hidden sm:inline text-zinc-500">{ranking.tag}</span>
                </Link>
              </TableCell>
              <TableCell className="text-xs sm:text-sm w-24 sm:w-36">
                <div className="w-24 sm:w-36">
                  {ranking.clubName}
                </div>
              </TableCell>
              <TableCell className="text-xs sm:text-sm sm:w-16 w-14">
                {ranking.trophies === 1 ? "100000+" : ranking.trophies}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </ScrollArea>
  );
}

function rankChangeColor(rankChange: number | null) {
  if (rankChange === null) {
    return "text-amber-500";
  } else if (rankChange > 0) {
    return "text-blue-500";
  } else if (rankChange < 0) {
    return "text-red-500";
  }

  return "text-zinc-500";
}

function rankChangeText(rankChange: number | null) {
  if (rankChange === null) {
    return "New";
  } else if (rankChange > 0) {
    return `+${rankChange}`;
  } else if (rankChange < 0) {
    return rankChange;
  }

  return "-";
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