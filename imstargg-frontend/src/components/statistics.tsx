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
import {BrawlerPairResultStatistics as BrawlersResultStatisticsModel} from "@/model/statistics/BrawlerPairResultStatistics";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import {BrawlerRole} from "@/model/enums/BrawlerRole";
import {BrawlerRarity} from "@/model/enums/BrawlerRarity";
import {BrawlerClassIcon} from "./brawler-class";
import {BrawlerLink} from "@/components/brawler-link";
import {BattleEvent} from "@/model/BattleEvent";
import BattleEventMapImage from "@/components/battle-event-map-image";
import {brawlerRarityTitle} from "@/lib/brawler-rarity";
import {battleEventModeIconSrc, battleEventModeTitle} from "@/lib/battle-mode";
import Image from "next/image";
import Link from "next/link";
import {battleEventHref} from "@/config/site";
import { Checkbox } from "./ui/checkbox";
import { useRouter, useSearchParams } from "next/navigation";
import { cn } from "@/lib/utils";

const toPercentage = (value: number): string => `${(value * 100).toFixed(2)}%`;

function BrawlerCell({brawler}: Readonly<{ brawler: Brawler | null }>) {
  return (
    <div className="flex flex-col sm:flex-row gap-1 sm:items-center items-start">
      <BrawlerLink brawler={brawler}>
        <BrawlerProfileImage
          brawler={brawler}
          size="sm"
        />
      </BrawlerLink>
      <div className="text-sm hidden sm:block">{brawler ? brawler.name : "❓"}</div>
    </div>
  )
}

function EventCell({event}: Readonly<{ event: BattleEvent }>) {
  const modeIconSrc = battleEventModeIconSrc(event.mode);
  return (
    <div className="flex gap-1">
      <Link href={battleEventHref(event.id)}>
        <BattleEventMapImage size="sm" battleEventMap={event.map}/>
      </Link>
      <div className="flex flex-col gap-1 justify-center">
        <div className="flex gap-1">
          {modeIconSrc && (
            <Image
              src={modeIconSrc}
              alt={`${event.mode} icon`}
              height={18}
            />)
          }
          <div className="text-xs sm:text-sm">{battleEventModeTitle(event.mode)}</div>
        </div>
        <div className="text-xs">{event.map.name}</div>
      </div>
    </div>
  )
}

function TextCell({value, className}: Readonly<{ value: string, className?: string }>) {
  return (
    <span className={cn("text-xs md:text-sm", className)}>
      {value}
    </span>
  )
}

type BrawlerRankStatisticsProps = {
  brawlers: Brawler[],
  selectedBrawlerId?: number,
  statsList: BrawlerRankStatisticsModel[]
};

export function EventBrawlerRankStatistics(
  {brawlers, selectedBrawlerId, statsList}: Readonly<BrawlerRankStatisticsProps>
) {
  const router = useRouter();
  const searchParams = useSearchParams();
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
      id: "brawlerSelect",
      cell: ({ row }) => (
        <Checkbox
          checked={row.original.brawlerId == selectedBrawlerId}
          onCheckedChange={(value) => {
            if (value) {
              const params = new URLSearchParams(searchParams);
              params.set('brawlerId', row.original.brawlerId.toString());
              router.replace(`?${params.toString()}`);
            }
          }}
          aria-label="Select row"
        />
      ),
      enableSorting: false,
      enableHiding: false,
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
    <DataTable columns={columns} data={statsList} pagination={{enabled: true}}/>
  )
}

type BrawlersRankStatisticsProps = {
  brawlers: Brawler[],
  statsList: BrawlersRankStatisticsModel[]
};

export function BrawlerPairRankStatistics(
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
        return (
          <BrawlerCell brawler={brawlerCollection.find(row.original.pairBrawlerId)}/>
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
    <DataTable columns={columns} data={statsList} pagination={{enabled: true}}/>
  )
}

type BrawlerResultStatisticsProps = {
  brawlers: Brawler[],
  selectedBrawlerId?: number,
  statsList: BrawlerResultStatisticsModel[]
};

export function EventBrawlerResultStatistics(
  {brawlers, selectedBrawlerId, statsList}: Readonly<BrawlerResultStatisticsProps>
) {
  const router = useRouter();
  const searchParams = useSearchParams();
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
      id: "brawlerSelect",
      cell: ({ row }) => (
        <Checkbox
          checked={row.original.brawlerId == selectedBrawlerId}
          onCheckedChange={(value) => {
            if (value) {
              const params = new URLSearchParams(searchParams);
              params.set('brawlerId', row.original.brawlerId.toString());
              router.replace(`?${params.toString()}`);
            }
          }}
          aria-label="Select row"
        />
      ),
      enableSorting: false,
      enableHiding: false,
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
    <DataTable columns={columns} data={statsList}/>
  )
}

type BrawlersResultStatisticsProps = {
  brawlers: Brawler[],
  statsList: BrawlersResultStatisticsModel[]
};

export function BrawlerPairResultStatistics(
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
        return (
          <BrawlerCell brawler={brawlerCollection.find(row.original.pairBrawlerId)}/>
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
    <DataTable columns={columns} data={statsList} pagination={{enabled: true}}/>
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

  const columns: ColumnDef<BrawlerListStatisticsData>[] = [
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
        <DataTableColumnHeader className="hidden sm:block" column={column} title={"유형"}/>,
      cell: ({row}) => <BrawlerClassIcon className="hidden sm:block" brawlerRole={row.original.brawler.role}/>,
    },
    {
      id: "brawlerRarity",
      accessorKey: "brawlerRarity",
      enableSorting: false,
      header: ({column}) =>
        <DataTableColumnHeader className="hidden sm:block" column={column} title={"희귀도"}/>,
      cell: ({row}) =>
        <TextCell className="hidden sm:block" value={brawlerRarityTitle(row.original.brawler.rarity)}/>,
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
    <DataTable columns={columns} data={statsList} pagination={{enabled: true}}/>
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
          <EventCell event={row.original.event}/>
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
    <DataTable columns={columns} data={statsList} pagination={{enabled: true}}/>
  )
}

