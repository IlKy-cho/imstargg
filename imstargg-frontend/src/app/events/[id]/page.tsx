import {getBattleEvent} from "@/lib/api/battle-event";
import {notFound} from "next/navigation";

type Props = {
  params: {
    id: number;
  }
};

export default async function EventPage({params}: Readonly<Props>) {
  const {id} = await params;
  const battleEvent = await getBattleEvent(id);
  if (!battleEvent) {
    notFound();
  }

  return (
    <div>
      <h1>{battleEvent.mode}</h1>
      <h2>{battleEvent.map.name}</h2>
    </div>
  );
};