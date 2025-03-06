import {
  getPlayerRenewalStatus,
  getPlayerRenewalStatusNew,
  PlayerRenewalStatusResponse,
  renewNewPlayer,
  renewPlayer
} from "@/lib/api/player";


async function handleRenewalStatus(tag: string, getStatus: (tag: string) => Promise<PlayerRenewalStatusResponse>) {
  const checkRenewalStatus = async () => {
    const status = await getStatus(tag);
    console.log("Renewal status:", status);
    if (!status.renewing) {
      console.log("Renewal finished");
      return Promise.resolve();
    } else {
      return new Promise(resolve => {
        setTimeout(async () => {
          await checkRenewalStatus().then(resolve);
        }, 1000);
      });
    }
  };

  await checkRenewalStatus();
}

export async function processPlayerRenewal(tag: string) {
  await renewPlayer(tag);
  await handleRenewalStatus(tag, getPlayerRenewalStatus);
}

export async function processNewPlayerRenewal(tag: string) {
  await renewNewPlayer(tag);
  await handleRenewalStatus(tag, getPlayerRenewalStatusNew);
}