"use client";

import {
  BrawlerRankStatistics as IBrawlerRankStatistics
} from "@/model/statistics/BrawlerRankStatistics";
import {
  BrawlersRankStatistics as IBrawlersRankStatistics
} from "@/model/statistics/BrawlersRankStatistics";
import {Brawler, BrawlerCollection} from "@/model/Brawler";
import {ColumnDef} from "@tanstack/react-table";
import {DataTableColumnHeader} from "@/components/ui/datatable/column-header";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import {DataTable} from "@/components/ui/datatable/data-table";
import {
  BrawlerResultStatistics as IBrawlerResultStatistics
} from "@/model/statistics/BrawlerResultStatistics";
import {
  BrawlersResultStatistics as IBrawlersResultStatistics
} from "@/model/statistics/BrawlersResultStatistics";

function toPercentage(value: number): string {
  return `${(value * 100).toFixed(2)}%`;
}

function BrawlerCell({brawler} : {brawler: Brawler | null}) {
  return (
    <div className="flex flex-col gap-1">
      <BrawlerProfileImage
        brawler={brawler}
        size="sm"
      />
      <div className="text-xs">{brawler ? brawler.name : "❓"}</div>
    </div>
  )
}

function BrawlersCell({brawlers} : {brawlers: Array<Brawler | null>}) {
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
  )
}

