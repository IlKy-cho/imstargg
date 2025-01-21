"use client";

import {BrawlerRankStatistics as IBrawlerRankStatistics} from "@/model/statistics/BrawlerRankStatistics";
import {BrawlersRankStatistics as IBrawlersRankStatistics} from "@/model/statistics/BrawlersRankStatistics";
import {Brawler, BrawlerCollection} from "@/model/Brawler";
import {ColumnDef} from "@tanstack/react-table";
import {DataTableColumnHeader} from "@/components/ui/datatable/column-header";
import {DataTable} from "@/components/ui/datatable/data-table";
import {
  BrawlerResultStatistics as IBrawlerResultStatistics
} from "@/model/statistics/BrawlerResultStatistics";
import {
  BrawlersResultStatistics as IBrawlersResultStatistics
} from "@/model/statistics/BrawlersResultStatistics";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import {BrawlerRole} from "@/model/enums/BrawlerRole";
import {BrawlerRarity} from "@/model/enums/BrawlerRarity";
import {BrawlerClassIcon} from "./brawler-class";
import {brawlerRarityTitle} from "./brawler-rarity";
import {BrawlerLink} from "@/components/brawler-link";

const toPercentage = (value: number): string => `${(value * 100).toFixed(2)}%`;

function BrawlerCell({brawler}: { brawler: Brawler | null }) {
  return (
    <div className="flex flex-col sm:flex-row gap-1 flex-grow items-center">
      <BrawlerProfileImage
        brawler={brawler}
        size="sm"
      />
      <div className="text-xs">{brawler ? brawler.name : "❓"}</div>
    </div>
  )
}

function BrawlersCell({brawlers}: { brawlers: Array<Brawler | null> }) {
  return (
    <div className="flex gap-1">
      {brawlers.map(brawler => (
        <BrawlerProfileImage
          key={brawler?.id}
          brawler={brawler}
          size="xs"
        />
      ))}
    </div>
  )
}

type BrawlerRankStatisticsProps = {
  brawlerList: Brawler[],
  brawlerRankStatsList: IBrawlerRankStatistics[]
};

export function BattleEventBrawlerRankStatistics(
  {brawlerList, brawlerRankStatsList}: Readonly<BrawlerRankStatisticsProps>
) {
  const brawlers = new BrawlerCollection(brawlerList);
  const columns: ColumnDef<IBrawlerRankStatistics>[] = [
    {
      accessorKey: "brawlerId",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"브롤러"}/>,
      cell: ({row}) => {
        const brawler = brawlers.find(row.original.brawlerId);
        return (
          <BrawlerCell brawler={brawler}/>
        );
      },
    },
    {
      accessorKey: "averageRank",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"평균 랭크"}/>,
      cell: ({row}) => row.original.averageRank.toFixed(2),
    },
    {
      accessorKey: "pickRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"픽률"}/>,
      cell: ({row}) => toPercentage(row.original.pickRate),
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) => row.original.totalBattleCount.toLocaleString(),
    }
  ];

  return (
    <DataTable columns={columns} data={brawlerRankStatsList}/>
  )
}

type BrawlersRankStatisticsProps = {
  brawlerList: Brawler[],
  brawlersRankStatsList: IBrawlersRankStatistics[]
};

export function BattleEventBrawlersRankStatistics(
  {brawlerList, brawlersRankStatsList}: Readonly<BrawlersRankStatisticsProps>
) {
  const brawlers = new BrawlerCollection(brawlerList);
  const columns: ColumnDef<IBrawlersRankStatistics>[] = [
    {
      accessorKey: "brawlerIds",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"브롤러 조합"}/>,
      cell: ({row}) => {
        const brawlersList = row.original.brawlerIds.map(id => brawlers.find(id));
        return (
          <BrawlersCell brawlers={brawlersList}/>
        );
      },
    },
    {
      accessorKey: "averageRank",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"평균 랭크"}/>,
      cell: ({row}) => row.original.averageRank.toFixed(2),
    },
    {
      accessorKey: "pickRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"픽률"}/>,
      cell: ({row}) => toPercentage(row.original.pickRate),
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) => row.original.totalBattleCount.toLocaleString(),
    }
  ];

  return (
    <DataTable columns={columns} data={brawlersRankStatsList} paginated={true}/>
  )
}

type BrawlerResultStatisticsProps = {
  brawlerList: Brawler[],
  brawlerResultStatsList: IBrawlerResultStatistics[]
};

export function BattleEventBrawlerResultStatistics(
  {brawlerList, brawlerResultStatsList}: Readonly<BrawlerResultStatisticsProps>
) {
  const brawlers = new BrawlerCollection(brawlerList);
  const columns: ColumnDef<IBrawlerResultStatistics>[] = [
    {
      accessorKey: "brawlerId",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"브롤러"}/>,
      cell: ({row}) => {
        const brawler = brawlers.find(row.original.brawlerId);
        return (
          <BrawlerCell brawler={brawler}/>
        );
      },
    },
    {
      accessorKey: "winRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"승률"}/>,
      cell: ({row}) => toPercentage(row.original.winRate),
    },
    {
      accessorKey: "starPlayerRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"스타플레이어"}/>,
      cell: ({row}) => toPercentage(row.original.starPlayerRate),
    },
    {
      accessorKey: "pickRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"픽률"}/>,
      cell: ({row}) => toPercentage(row.original.pickRate),
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) => row.original.totalBattleCount.toLocaleString(),
    }
  ];

  return (
    <DataTable columns={columns} data={brawlerResultStatsList}/>
  )
}

type BrawlersResultStatisticsProps = {
  brawlerList: Brawler[],
  brawlersResultStatsList: IBrawlersResultStatistics[]
};

export function BattleEventBrawlersResultStatistics(
  {brawlerList, brawlersResultStatsList}: Readonly<BrawlersResultStatisticsProps>
) {
  const brawlers = new BrawlerCollection(brawlerList);
  const columns: ColumnDef<IBrawlersResultStatistics>[] = [
    {
      accessorKey: "brawlerIds",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"브롤러 조합"}/>,
      cell: ({row}) => {
        const brawlersList = row.original.brawlerIds.map(id => brawlers.find(id));
        return (
          <BrawlersCell brawlers={brawlersList}/>
        );
      },
    },
    {
      accessorKey: "winRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"승률"}/>,
      cell: ({row}) => toPercentage(row.original.winRate),
    },
    {
      accessorKey: "pickRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"픽률"}/>,
      cell: ({row}) => toPercentage(row.original.pickRate),
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) => row.original.totalBattleCount.toLocaleString(),
    }
  ];

  return (
    <DataTable columns={columns} data={brawlersResultStatsList} paginated={true}/>
  );
}

type BrawlerListStatisticsProps = {
  brawlers: Brawler[];
  resultStatisticsList: IBrawlerResultStatistics[];
}

type BrawlerListStatisticsData = {
  brawler: Brawler;
  brawlerRole: BrawlerRole;
  brawlerRarity: BrawlerRarity;
  totalBattleCount: number | null;
  winRate: number | null;
  pickRate: number | null;
  starPlayerRate: number | null;
}

export function BrawlerListStatistics(
  {brawlers, resultStatisticsList}: Readonly<BrawlerListStatisticsProps>
) {

  const statisticsMap: Record<number, IBrawlerResultStatistics> = resultStatisticsList.reduce(
    (acc, stats) => {
      acc[stats.brawlerId] = stats;
      return acc;
    },
    {} as Record<number, IBrawlerResultStatistics>
  );

  const data: BrawlerListStatisticsData[] = brawlers.map(brawler => {
    const resultStatistics = statisticsMap[brawler.id] || null;

    return {
      brawler,
      brawlerRole: brawler.role,
      brawlerRarity: brawler.rarity,
      totalBattleCount: resultStatistics ? resultStatistics.totalBattleCount : null,
      winRate: resultStatistics ? resultStatistics.winRate : null,
      pickRate: resultStatistics ? resultStatistics.pickRate : null,
      starPlayerRate: resultStatistics ? resultStatistics.starPlayerRate : null,
    };
  });

  const columns: ColumnDef<BrawlerListStatisticsData>[] = [
    {
      accessorKey: "brawler",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"브롤러"}/>,
      cell: ({row}) => {
        return (
          <BrawlerLink brawler={row.original.brawler}>
            <BrawlerCell brawler={row.original.brawler}/>
          </BrawlerLink>
        );
      },
    },
    {
      accessorKey: "brawlerRole",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"유형"}/>,
      cell: ({row}) => <BrawlerClassIcon brawlerRole={row.original.brawler.role}/>,
    },
    {
      accessorKey: "brawlerRarity",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"희귀도"}/>,
      cell: ({row}) => brawlerRarityTitle(row.original.brawler.rarity),
    },
    {
      accessorKey: "winRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"승률"}/>,
      cell: ({row}) => row.original.winRate !== null ? toPercentage(row.original.winRate) : "N/A",
    },
    {
      accessorKey: "pickRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"픽률"}/>,
      cell: ({row}) => row.original.pickRate !== null ? toPercentage(row.original.pickRate) : "N/A",
    },
    {
      accessorKey: "starPlayerRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"스타플레이어"}/>,
      cell: ({row}) => row.original.starPlayerRate !== null ? toPercentage(row.original.starPlayerRate) : "N/A",
    },
    {
      accessorKey: "totalBattleCount",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) => row.original.totalBattleCount?.toLocaleString() || "N/A",
    },
  ];

  return (
    <DataTable columns={columns} data={data}/>
  );
}

