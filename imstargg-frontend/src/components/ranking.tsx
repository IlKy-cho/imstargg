import {PlayerRanking as PlayerRankingModel} from "@/model/PlayerRanking";
import {Country} from "@/model/enums/Country";
import {CountrySelect} from "@/components/ranking-option";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import {ScrollArea} from "@/components/ui/scroll-area";

interface RankingProps extends RankingListProps, RankingTitleProps {}

export async function Ranking({rankings, country}: Readonly<RankingProps>) {
  return (
    <div className="flex flex-col p-2 gap-2 border border-zinc-200 rounded">
      <RankingTitle country={country}/>
      <RankingList rankings={rankings}/>
    </div>
  )
}

interface RankingListProps {
  rankings: PlayerRankingModel[];
}

export async function RankingList({rankings}: Readonly<RankingListProps>) {

  return (
    <ScrollArea className="h-80 sm:h-96">
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
              <TableCell>{ranking.rank}</TableCell>
              <TableCell>{ranking.name}</TableCell>
              <TableCell>{ranking.clubName}</TableCell>
              <TableCell>{ranking.trophies}</TableCell>
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