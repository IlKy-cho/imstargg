"use client";

import {BrawlerRankStatistics as BrawlerRankStatisticsModel} from "@/model/statistics/BrawlerRankStatistics";
import {BrawlersRankStatistics as BrawlersRankStatisticsModel} from "@/model/statistics/BrawlersRankStatistics";
import {
  BrawlerEnemyResultStatistics as BrawlerEnemyResultStatisticsModel
} from "@/model/statistics/BrawlerEnemyResultStatistics";
import {
  BattleEventResultStatistics as BattleEventResultStatisticsModel
} from "@/model/statistics/BattleEventResultStatistics";
import {Brawler, BrawlerCollection} from "@/model/Brawler";
import {ColumnDef} from "@tanstack/react-table";
import {DataTableColumnHeader} from "@/components/ui/datatable/column-header";
import {DataTable} from "@/components/ui/datatable/data-table";
import {BrawlerResultStatistics as BrawlerResultStatisticsModel} from "@/model/statistics/BrawlerResultStatistics";
import {BrawlersResultStatistics as BrawlersResultStatisticsModel} from "@/model/statistics/BrawlersResultStatistics";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import {BrawlerRole} from "@/model/enums/BrawlerRole";
import {BrawlerRarity} from "@/model/enums/BrawlerRarity";
import {BrawlerClassIcon} from "./brawler-class";
import {brawlerRarityTitle} from "./brawler-rarity";
import {BrawlerLink} from "@/components/brawler-link";
import {useMediaQuery} from "usehooks-ts";

const toPercentage = (value: number): string => `${(value * 100).toFixed(2)}%`;

function BrawlerCell({brawler}: { brawler: Brawler | null }) {
  return (
    <div className="flex flex-col sm:flex-row gap-1">
      <BrawlerLink brawler={brawler}>
        <BrawlerProfileImage
          brawler={brawler}
          size="sm"
        />
      </BrawlerLink>
      <div className="md:text-sm text-xs">{brawler ? brawler.name : "❓"}</div>
    </div>
  )
}

function BrawlersCell({brawlers}: { brawlers: Array<Brawler | null> }) {
  return (
    <div className="flex gap-1">
      {brawlers.map(brawler => (
        <BrawlerLink brawler={brawler} key={brawler?.id}>
          <BrawlerProfileImage
            brawler={brawler}
            size="xs"
          />
        </BrawlerLink>
      ))}
    </div>
  )
}

function TextCell({value}: { value: string }) {
  return (
    <span className="text-xs md:text-sm">
      {value}
    </span>
  )
}

type BrawlerRankStatisticsProps = {
  brawlers: Brawler[],
  statsList: BrawlerRankStatisticsModel[]
};

export function BrawlerRankStatistics(
  {brawlers, statsList}: Readonly<BrawlerRankStatisticsProps>
) {
  const brawlerCollection = new BrawlerCollection(brawlers);
  const columns: ColumnDef<BrawlerRankStatisticsModel>[] = [
    {
      accessorKey: "brawlerId",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"브롤러"}/>,
      cell: ({row}) => {
        const brawler = brawlerCollection.find(row.original.brawlerId);
        return (
          <BrawlerCell brawler={brawler}/>
        );
      },
    },
    {
      accessorKey: "averageRank",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"평균 랭크"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.averageRank.toFixed(2)}/>,
    },
    {
      accessorKey: "pickRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"픽률"}/>,
      cell: ({row}) =>
        <TextCell value={toPercentage(row.original.pickRate)}/>,
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.totalBattleCount.toLocaleString()}/>,
    }
  ];

  return (
    <DataTable columns={columns} data={statsList} pagination={{ enabled: true }} />
  )
}

type BrawlersRankStatisticsProps = {
  brawlers: Brawler[],
  statsList: BrawlersRankStatisticsModel[]
};

export function BrawlersRankStatistics(
  {brawlers, statsList}: Readonly<BrawlersRankStatisticsProps>
) {
  const brawlerCollection = new BrawlerCollection(brawlers);
  const columns: ColumnDef<BrawlersRankStatisticsModel>[] = [
    {
      accessorKey: "brawlerIds",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"브롤러 조합"}/>,
      cell: ({row}) => {
        const brawlersList = row.original.brawlerIds.map(id => brawlerCollection.find(id));
        return (
          <BrawlersCell brawlers={brawlersList}/>
        );
      },
    },
    {
      accessorKey: "averageRank",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"평균 랭크"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.averageRank.toFixed(2)}/>,
    },
    {
      accessorKey: "pickRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"픽률"}/>,
      cell: ({row}) =>
        <TextCell value={toPercentage(row.original.pickRate)}/>,
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.totalBattleCount.toLocaleString()}/>,
    }
  ];

  return (
    <DataTable columns={columns} data={statsList} pagination={{ enabled: true }} />
  )
}

type BrawlerResultStatisticsProps = {
  brawlers: Brawler[],
  statsList: BrawlerResultStatisticsModel[]
};

export function BrawlerResultStatistics(
  {brawlers, statsList}: Readonly<BrawlerResultStatisticsProps>
) {
  const brawlerCollection = new BrawlerCollection(brawlers);
  const columns: ColumnDef<BrawlerResultStatisticsModel>[] = [
    {
      accessorKey: "brawlerId",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"브롤러"}/>,
      cell: ({row}) => {
        const brawler = brawlerCollection.find(row.original.brawlerId);
        return (
          <BrawlerCell brawler={brawler}/>
        );
      },
    },
    {
      accessorKey: "winRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"승률"}/>,
      cell: ({row}) =>
        <TextCell value={toPercentage(row.original.winRate)}/>,
    },
    {
      accessorKey: "starPlayerRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"Star"}/>,
      cell: ({row}) =>
        <TextCell value={toPercentage(row.original.starPlayerRate)}/>,
    },
    {
      accessorKey: "pickRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"픽률"}/>,
      cell: ({row}) =>
        <TextCell value={toPercentage(row.original.pickRate)}/>,
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.totalBattleCount.toLocaleString()}/>,
    }
  ];

  return (
    <DataTable columns={columns} data={statsList} pagination={{ enabled: true }} />
  )
}

type BrawlersResultStatisticsProps = {
  brawlers: Brawler[],
  statsList: BrawlersResultStatisticsModel[]
};

export function BrawlersResultStatistics(
  {brawlers, statsList}: Readonly<BrawlersResultStatisticsProps>
) {
  const brawlerCollection = new BrawlerCollection(brawlers);
  const columns: ColumnDef<BrawlersResultStatisticsModel>[] = [
    {
      accessorKey: "brawlerIds",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"브롤러 조합"}/>,
      cell: ({row}) => {
        const brawlersList = row.original.brawlerIds.map(id => brawlerCollection.find(id));
        return (
          <BrawlersCell brawlers={brawlersList}/>
        );
      },
    },
    {
      accessorKey: "winRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"승률"}/>,
      cell: ({row}) =>
        <TextCell value={toPercentage(row.original.winRate)}/>,
    },
    {
      accessorKey: "pickRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"픽률"}/>,
      cell: ({row}) =>
        <TextCell value={toPercentage(row.original.pickRate)}/>,
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.totalBattleCount.toLocaleString()}/>,
    }
  ];

  return (
    <DataTable columns={columns} data={statsList} pagination={{ enabled: true }} />
  );
}

type BrawlerListStatisticsProps = {
  brawlers: Brawler[];
  statsList: BrawlerResultStatisticsModel[];
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
  {brawlers, statsList}: Readonly<BrawlerListStatisticsProps>
) {

  const statisticsMap: Record<number, BrawlerResultStatisticsModel> = statsList.reduce(
    (acc, stats) => {
      acc[stats.brawlerId] = stats;
      return acc;
    },
    {} as Record<number, BrawlerResultStatisticsModel>
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

  let columns: ColumnDef<BrawlerListStatisticsData>[] = [
    {
      accessorKey: "brawler",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"브롤러"}/>,
      cell: ({row}) => {
        return (
          <BrawlerCell brawler={row.original.brawler}/>
        );
      },
    },
    {
      id: "brawlerRole",
      accessorKey: "brawlerRole",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"유형"}/>,
      cell: ({row}) => <BrawlerClassIcon brawlerRole={row.original.brawler.role}/>,
    },
    {
      id: "brawlerRarity",
      accessorKey: "brawlerRarity",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"희귀도"}/>,
      cell: ({row}) =>
        <TextCell value={brawlerRarityTitle(row.original.brawler.rarity)}/>,
    },
    {
      accessorKey: "winRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"승률"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.winRate !== null ? toPercentage(row.original.winRate) : "N/A"}/>,
    },
    {
      accessorKey: "pickRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"픽률"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.pickRate !== null ? toPercentage(row.original.pickRate) : "N/A"}/>,
    },
    {
      accessorKey: "starPlayerRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"Star"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.starPlayerRate !== null ? toPercentage(row.original.starPlayerRate) : "N/A"}/>,
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.totalBattleCount?.toLocaleString() || "N/A"}/>,
    },
  ];

  const isMobile = useMediaQuery('(max-width: 480px)');
  if (isMobile) {
    columns = columns
      .filter(column => column.id !== "brawlerRole")
      .filter(column => column.id !== "brawlerRarity");
  }

  return (
    <DataTable columns={columns} data={data}/>
  );
}

type BrawlerEnemyResultStatisticsProps = {
  brawlers: Brawler[];
  statsList: BrawlerEnemyResultStatisticsModel[];
}

export function BrawlerEnemyResultStatistics(
  {brawlers, statsList}: Readonly<BrawlerEnemyResultStatisticsProps>
) {
  const brawlerCollection = new BrawlerCollection(brawlers);
  const columns: ColumnDef<BrawlerEnemyResultStatisticsModel>[] = [
    {
      accessorKey: "enemyBrawlerId",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"상대 브롤러"}/>,
      cell: ({row}) => {
        const enemyBrawler = brawlerCollection.find(row.original.enemyBrawlerId);
        return (
          <BrawlerCell brawler={enemyBrawler}/>
        );
      },
    },
    {
      accessorKey: "winRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"승률"}/>,
      cell: ({row}) =>
        <TextCell value={toPercentage(row.original.winRate)}/>,
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.totalBattleCount.toLocaleString()}/>,
    }
  ];

  return (
    <DataTable columns={columns} data={statsList} pagination={{ enabled: true }} />
  )
}

type BattleEventResultStatisticsProps = {
  statsList: BattleEventResultStatisticsModel[];
}

export function BattleEventResultStatistics(
  {statsList}: Readonly<BattleEventResultStatisticsProps>
) {
  const columns: ColumnDef<BattleEventResultStatisticsModel>[] = [
    {
      accessorKey: "event",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"이벤트"}/>,
      cell: ({row}) => {
        return (
          <TextCell value={row.original.event.map.name || "❓"}/>
        );
      },
    },
    {
      accessorKey: "winRate",
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"승률"}/>,
      cell: ({row}) =>
        <TextCell value={toPercentage(row.original.winRate)}/>,
    },
    {
      accessorKey: "totalBattleCount",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader column={column} title={"표본수"}/>,
      cell: ({row}) =>
        <TextCell value={row.original.totalBattleCount.toLocaleString()}/>,
    }
  ];

  return (
    <DataTable columns={columns} data={statsList} pagination={{ enabled: true, size: 5 }} />
  )
}

