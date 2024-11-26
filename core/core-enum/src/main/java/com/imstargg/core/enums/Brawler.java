package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Brawler {

    // TODO 하이퍼차지, 이동속도, 사정거리, 재장전 속도, 공격력, 체력...

    UNKNOWN(0, "UNKNOWN", null, null, null, null, null),

    SHELLY(
            16000000, "SHELLY", BrawlerRarity.STARTING_BRAWLER, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.FAST_FORWARD, Gadget.CLAY_PIGEONS),
            Gear.gearListWith(),
            List.of(StarPower.SHELL_SHOCK, StarPower.BAND_AID)
    ),
    COLT(
            16000001, "COLT", BrawlerRarity.RARE, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.SPEEDLOADER, Gadget.SILVER_BULLET),
            Gear.gearListWith(Gear.RELOAD_SPEED),
            List.of(StarPower.SLICK_BOOTS, StarPower.MAGNUM_SPECIAL)
    ),
    BULL(
            16000002, "BULL", BrawlerRarity.RARE, BrawlerRole.TANK,
            List.of(Gadget.T_BONE_INJECTOR, Gadget.STOMPER),
            Gear.gearListWith(Gear.SUPER_CHARGE),
            List.of(StarPower.BERSERKER, StarPower.TOUGH_GUY)
    ),
    BROCK(
            16000003, "BROCK", BrawlerRarity.RARE, BrawlerRole.MARKSMAN,
            List.of(Gadget.ROCKET_LACES, Gadget.ROCKET_FUEL),
            Gear.gearListWith(Gear.RELOAD_SPEED),
            List.of(StarPower.MORE_ROCKETS, StarPower.ROCKET_NO_4)
    ),
    RICO(
            16000004, "RICO", BrawlerRarity.SUPER_RARE, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.MULTIBALL_LAUNCHER, Gadget.BOUNCY_CASTLE),
            Gear.gearListWith(Gear.RELOAD_SPEED),
            List.of(StarPower.SUPER_BOUNCY, StarPower.ROBO_RETREAT)
    ),
    SPIKE(
            16000005, "SPIKE", BrawlerRarity.LEGENDARY, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.POPPING_PINCUSHION, Gadget.LIFE_PLANT),
            Gear.gearListWith(Gear.STICKY_SPIKES),
            List.of(StarPower.FERTILIZE, StarPower.CURVEBALL)
    ),
    BARLEY(
            16000006, "BARLEY", BrawlerRarity.RARE, BrawlerRole.ARTILLERY,
            List.of(Gadget.STICKY_SYRUP_MIXER, Gadget.HERBAL_TONIC),
            Gear.gearListWith(),
            List.of(StarPower.MEDICAL_USE, StarPower.EXTRA_NOXIOUS)
    ),
    JESSIE(
            16000007, "JESSIE", BrawlerRarity.SUPER_RARE, BrawlerRole.CONTROLLER,
            List.of(Gadget.SPARK_PLUG, Gadget.RECOIL_SPRING),
            Gear.gearListWith(Gear.PET_POWER),
            List.of(StarPower.ENERGIZE, StarPower.SHOCKY)
    ),
    NITA(
            16000008, "NITA", BrawlerRarity.RARE, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.BEAR_PAWS, Gadget.FAUX_FUR),
            Gear.gearListWith(Gear.PET_POWER),
            List.of(StarPower.BEAR_WITH_ME, StarPower.HYPER_BEAR)
    ),
    DYNAMIKE(
            16000009, "DYNAMIKE", BrawlerRarity.SUPER_RARE, BrawlerRole.ARTILLERY,
            List.of(Gadget.FIDGET_SPINNER, Gadget.SATCHEL_CHARGE),
            Gear.gearListWith(),
            List.of(StarPower.DYNA_JUMP, StarPower.DEMOLITION)
    ),
    EL_PRIMO(
            16000010, "EL PRIMO", BrawlerRarity.RARE, BrawlerRole.TANK,
            List.of(Gadget.SUPLEX_SUPPLEMENT, Gadget.ASTEROID_BELT),
            Gear.gearListWith(Gear.SUPER_CHARGE),
            List.of(StarPower.EL_FUEGO, StarPower.METEOR_RUSH)
    ),
    MORTIS(
            16000011, "MORTIS", BrawlerRarity.MYTHIC, BrawlerRole.ASSASSIN,
            List.of(Gadget.COMBO_SPINNER, Gadget.SURVIVAL_SHOVEL),
            Gear.gearListWith(Gear.BAT_STORM),
            List.of(StarPower.CREEPY_HARVEST, StarPower.COILED_SNAKE)
    ),
    CROW(
            16000012, "CROW", BrawlerRarity.LEGENDARY, BrawlerRole.ASSASSIN,
            List.of(Gadget.DEFENSE_BOOSTER, Gadget.SLOWING_TOXIN),
            Gear.gearListWith(Gear.ENDURING_TOXIN),
            List.of(StarPower.EXTRA_TOXIC, StarPower.CARRION_CROW)
    ),
    POCO(
            16000013, "POCO", BrawlerRarity.RARE, BrawlerRole.SUPPORT,
            List.of(Gadget.TUNING_FORK, Gadget.PROTECTIVE_TUNES),
            Gear.gearListWith(),
            List.of(StarPower.DA_CAPO, StarPower.SCREECHING_SOLO)
    ),
    BO(
            16000014, "BO", BrawlerRarity.EPIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.SUPER_TOTEM, Gadget.TRIPWIRE),
            Gear.gearListWith(Gear.RELOAD_SPEED),
            List.of(StarPower.CIRCLING_EAGLE, StarPower.SNARE_A_BEAR)
    ),
    PIPER(
            16000015, "PIPER", BrawlerRarity.EPIC, BrawlerRole.MARKSMAN,
            List.of(Gadget.AUTO_AIMER, Gadget.HOMEMADE_RECIPE),
            Gear.gearListWith(),
            List.of(StarPower.AMBUSH, StarPower.SNAPPY_SNIPING)
    ),
    PAM(
            16000016, "PAM", BrawlerRarity.EPIC, BrawlerRole.SUPPORT,
            List.of(Gadget.PULSE_MODULATOR, Gadget.SCRAPSUCKER),
            Gear.gearListWith(Gear.SUPER_TURRET),
            List.of(StarPower.MAMAS_HUG, StarPower.MAMAS_SQUEEZE)
    ),
    TARA(
            16000017, "TARA", BrawlerRarity.MYTHIC, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.PSYCHIC_ENHANCER, Gadget.SUPPORT_FROM_BEYOND),
            Gear.gearListWith(Gear.PET_POWER),
            List.of(StarPower.BLACK_PORTAL, StarPower.HEALING_SHADE)
    ),
    DARRYL(
            16000018, "DARRYL", BrawlerRarity.SUPER_RARE, BrawlerRole.TANK,
            List.of(Gadget.RECOILING_ROTATOR, Gadget.TAR_BARREL),
            Gear.gearListWith(),
            List.of(StarPower.STEEL_HOOPS, StarPower.ROLLING_RELOAD)
    ),
    PENNY(
            16000019, "PENNY", BrawlerRarity.SUPER_RARE, BrawlerRole.CONTROLLER,
            List.of(Gadget.SALTY_BARREL, Gadget.TRUSTY_SPYGLASS),
            Gear.gearListWith(Gear.PET_POWER),
            List.of(StarPower.HEAVY_COFFERS, StarPower.MASTER_BLASTER)
    ),
    FRANK(
            16000020, "FRANK", BrawlerRarity.EPIC, BrawlerRole.TANK,
            List.of(Gadget.ACTIVE_NOISE_CANCELING, Gadget.IRRESISTIBLE_ATTRACTION),
            Gear.gearListWith(),
            List.of(StarPower.POWER_GRAB, StarPower.SPONGE)
    ),
    GENE(
            16000021, "GENE", BrawlerRarity.MYTHIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.LAMP_BLOWOUT, Gadget.VENGEFUL_SPIRITS),
            Gear.gearListWith(Gear.TALK_TO_THE_HAND),
            List.of(StarPower.MAGIC_PUFFS, StarPower.SPIRIT_SLAP)
    ),
    TICK(
            16000022, "TICK", BrawlerRarity.SUPER_RARE, BrawlerRole.ARTILLERY,
            List.of(Gadget.MINE_MANIA, Gadget.LAST_HURRAH),
            Gear.gearListWith(Gear.THICC_HEAD),
            List.of(StarPower.WELL_OILED, StarPower.AUTOMA_TICK_RELOAD)
    ),
    LEON(
            16000023, "LEON", BrawlerRarity.LEGENDARY, BrawlerRole.ASSASSIN,
            List.of(Gadget.CLONE_PROJECTOR, Gadget.LOLLIPOP_DROP),
            Gear.gearListWith(Gear.LINGERING_SMOKE),
            List.of(StarPower.SMOKE_TRAILS, StarPower.INVISIHEAL)
    ),
    ROSA(
            16000024, "ROSA", BrawlerRarity.RARE, BrawlerRole.TANK,
            List.of(Gadget.GROW_LIGHT, Gadget.UNFRIENDLY_BUSHES),
            Gear.gearListWith(),
            List.of(StarPower.PLANT_LIFE, StarPower.THORNY_GLOVES)
    ),
    CARL(
            16000025, "CARL", BrawlerRarity.SUPER_RARE, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.HEAT_EJECTOR, Gadget.FLYING_HOOK),
            Gear.gearListWith(),
            List.of(StarPower.POWER_THROW, StarPower.PROTECTIVE_PIROUETTE)
    ),
    BIBI(
            16000026, "BIBI", BrawlerRarity.EPIC, BrawlerRole.TANK,
            List.of(Gadget.VITAMIN_BOOSTER, Gadget.EXTRA_STICKY),
            Gear.gearListWith(),
            List.of(StarPower.HOME_RUN, StarPower.BATTING_STANCE)
    ),
    EIGHT_BIT(
            16000027, "8-BIT", BrawlerRarity.SUPER_RARE, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.CHEAT_CARTRIDGE, Gadget.EXTRA_CREDITS),
            Gear.gearListWith(Gear.RELOAD_SPEED),
            List.of(StarPower.BOOSTED_BOOSTER, StarPower.PLUGGED_IN)
    ),
    SANDY(
            16000028, "SANDY", BrawlerRarity.LEGENDARY, BrawlerRole.CONTROLLER,
            List.of(Gadget.SLEEP_STIMULATOR, Gadget.SWEET_DREAMS),
            Gear.gearListWith(Gear.EXHAUSTING_STORM),
            List.of(StarPower.RUDE_SANDS, StarPower.HEALING_WINDS)
    ),
    BEA(
            16000029, "BEA", BrawlerRarity.EPIC, BrawlerRole.MARKSMAN,
            List.of(Gadget.HONEY_MOLASSES, Gadget.RATTLED_HIVE),
            Gear.gearListWith(),
            List.of(StarPower.INSTA_BEALOAD, StarPower.HONEYCOMB)
    ),
    EMZ(
            16000030, "EMZ", BrawlerRarity.EPIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.FRIENDZONER, Gadget.ACID_SPRAY),
            Gear.gearListWith(),
            List.of(StarPower.BAD_KARMA, StarPower.HYPE)
    ),
    MR_P(
            16000031, "MR. P", BrawlerRarity.MYTHIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.SERVICE_BELL, Gadget.PORTER_REINFORCEMENTS),
            Gear.gearListWith(Gear.PET_POWER),
            List.of(StarPower.HANDLE_WITH_CARE, StarPower.REVOLVING_DOOR)
    ),
    MAX(
            16000032, "MAX", BrawlerRarity.MYTHIC, BrawlerRole.SUPPORT,
            List.of(Gadget.PHASE_SHIFTER, Gadget.SNEAKY_SNEAKERS),
            Gear.gearListWith(),
            List.of(StarPower.SUPER_CHARGED, StarPower.RUN_N_GUN)

    ),
    JACKY(
            16000034, "JACKY", BrawlerRarity.SUPER_RARE, BrawlerRole.TANK,
            List.of(Gadget.PNEUMATIC_BOOSTER, Gadget.REBUILD),
            Gear.gearListWith(Gear.SUPER_CHARGE),
            List.of(StarPower.COUNTER_CRUSH, StarPower.HARDY_HARD_HAT)
    ),
    GALE(
            16000035, "GALE", BrawlerRarity.EPIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.SPRING_EJECTOR, Gadget.TWISTER),
            Gear.gearListWith(),
            List.of(StarPower.BLUSTERY_BLOW, StarPower.FREEZING_SNOW)
    ),
    NANI(
            16000036, "NANI", BrawlerRarity.EPIC, BrawlerRole.MARKSMAN,
            List.of(Gadget.WARPIN_TIME, Gadget.RETURN_TO_SENDER),
            Gear.gearListWith(Gear.SUPER_CHARGE),
            List.of(StarPower.AUTOFOCUS, StarPower.TEMPERED_STEEL)
    ),
    SPROUT(
            16000037, "SPROUT", BrawlerRarity.MYTHIC, BrawlerRole.ARTILLERY,
            List.of(Gadget.GARDEN_MULCHER, Gadget.TRANSPLANT),
            Gear.gearListWith(Gear.SUPER_CHARGE),
            List.of(StarPower.OVERGROWTH, StarPower.PHOTOSYNTHESIS)
    ),
    SURGE(
            16000038, "SURGE", BrawlerRarity.LEGENDARY, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.POWER_SURGE, Gadget.POWER_SHIELD),
            Gear.gearListWith(),
            List.of(StarPower.TO_THE_MAX, StarPower.SERVE_ICE_COLD)
    ),
    COLETTE(
            16000039, "COLETTE", BrawlerRarity.EPIC, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.NA_AH, Gadget.GOTCHA),
            Gear.gearListWith(),
            List.of(StarPower.PUSH_IT, StarPower.MASS_TAX)
    ),
    AMBER(
            16000040, "AMBER", BrawlerRarity.LEGENDARY, BrawlerRole.CONTROLLER,
            List.of(Gadget.FIRE_STARTERS, Gadget.DANCING_FLAMES),
            Gear.gearListWith(Gear.RELOAD_SPEED, Gear.STICKY_OIL),
            List.of(StarPower.WILD_FLAMES, StarPower.SCORCHIN_SIPHON)
    ),
    LOU(
            16000041, "LOU", BrawlerRarity.MYTHIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.ICE_BLOCK, Gadget.CRYO_SYRUP),
            Gear.gearListWith(Gear.SUPER_CHARGE),
            List.of(StarPower.SUPERCOOL, StarPower.HYPOTHERMIA)
    ),
    BYRON(
            16000042, "BYRON", BrawlerRarity.MYTHIC, BrawlerRole.SUPPORT,
            List.of(Gadget.SHOT_IN_THE_ARM, Gadget.BOOSTER_SHOTS),
            Gear.gearListWith(),
            List.of(StarPower.MALAISE, StarPower.INJECTION)
    ),
    EDGAR(
            16000043, "EDGAR", BrawlerRarity.EPIC, BrawlerRole.ASSASSIN,
            List.of(Gadget.LETS_FLY, Gadget.HARDCORE),
            Gear.gearListWith(Gear.SUPER_CHARGE),
            List.of(StarPower.HARD_LANDING, StarPower.FISTICUFFS)
    ),
    RUFFS(
            16000044, "RUFFS", BrawlerRarity.MYTHIC, BrawlerRole.SUPPORT,
            List.of(Gadget.TAKE_COVER, Gadget.AIR_SUPPORT),
            Gear.gearListWith(),
            List.of(StarPower.AIR_SUPERIORITY, StarPower.FIELD_PROMOTION)
    ),
    STU(
            16000045, "STU", BrawlerRarity.EPIC, BrawlerRole.ASSASSIN,
            List.of(Gadget.SPEED_ZONE, Gadget.BREAKTHROUGH),
            Gear.gearListWith(),
            List.of(StarPower.ZERO_DRAG, StarPower.GASO_HEAL)
    ),
    BELLE(
            16000046, "BELLE", BrawlerRarity.EPIC, BrawlerRole.MARKSMAN,
            List.of(Gadget.NEST_EGG, Gadget.REVERSE_POLARITY),
            Gear.gearListWith(Gear.RELOAD_SPEED),
            List.of(StarPower.POSITIVE_FEEDBACK, StarPower.GROUNDED)

    ),
    SQUEAK(
            16000047, "SQUEAK", BrawlerRarity.MYTHIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.WINDUP, Gadget.RESIDUE),
            Gear.gearListWith(),
            List.of(StarPower.CHAIN_REACTION, StarPower.SUPER_STICKY)
    ),
    GROM(
            16000048, "GROM", BrawlerRarity.EPIC, BrawlerRole.ARTILLERY,
            List.of(Gadget.WATCHTOWER, Gadget.RADIO_CHECK),
            Gear.gearListWith(),
            List.of(StarPower.FOOT_PATROL, StarPower.X_FACTOR)
    ),
    BUZZ(
            16000049, "BUZZ", BrawlerRarity.MYTHIC, BrawlerRole.ASSASSIN,
            List.of(Gadget.RESERVE_BUOY, Gadget.X_RAY_SHADES),
            Gear.gearListWith(),
            List.of(StarPower.TOUGHER_TORPEDO, StarPower.EYES_SHARP)
    ),
    GRIFF(
            16000050, "GRIFF", BrawlerRarity.EPIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.PIGGY_BANK, Gadget.COIN_SHOWER),
            Gear.gearListWith(Gear.RELOAD_SPEED),
            List.of(StarPower.KEEP_THE_CHANGE, StarPower.BUSINESS_RESILIENCE)
    ),
    ASH(
            16000051, "ASH", BrawlerRarity.EPIC, BrawlerRole.TANK,
            List.of(Gadget.CHILL_PILL, Gadget.ROTTEN_BANANA),
            Gear.gearListWith(),
            List.of(StarPower.FIRST_BASH, StarPower.MAD_AS_HECK)
    ),
    MEG(
            16000052, "MEG", BrawlerRarity.LEGENDARY, BrawlerRole.TANK,
            List.of(Gadget.JOLTING_VOLTS, Gadget.TOOLBOX),
            Gear.gearListWith(),
            List.of(StarPower.FORCE_FIELD, StarPower.HEAVY_METAL)
    ),
    LOLA(
            16000053, "LOLA", BrawlerRarity.EPIC, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.FREEZE_FRAME, Gadget.STUNT_DOUBLE),
            Gear.gearListWith(Gear.RELOAD_SPEED),
            List.of(StarPower.IMPROVISE, StarPower.SEALED_WITH_A_KISS)
    ),
    FANG(
            16000054, "FANG", BrawlerRarity.MYTHIC, BrawlerRole.ASSASSIN,
            List.of(Gadget.CORN_FU, Gadget.ROUNDHOUSE_KICK),
            Gear.gearListWith(),
            List.of(StarPower.FRESH_KICKS, StarPower.DIVINE_SOLES)
    ),
    EVE(
            16000056, "EVE", BrawlerRarity.MYTHIC, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.GOTTA_GO, Gadget.MOTHERLY_LOVE),
            Gear.gearListWith(Gear.RELOAD_SPEED, Gear.QUADRUPLETS),
            List.of(StarPower.UNNATURAL_ORDER, StarPower.HAPPY_SURPRISE)
    ),
    JANET(
            16000057, "JANET", BrawlerRarity.MYTHIC, BrawlerRole.MARKSMAN,
            List.of(Gadget.DROP_THE_BASS, Gadget.BACKSTAGE_PASS),
            Gear.gearListWith(),
            List.of(StarPower.STAGE_VIEW, StarPower.VOCAL_WARM_UP)
    ),
    BONNIE(
            16000058, "BONNIE", BrawlerRarity.EPIC, BrawlerRole.MARKSMAN,
            List.of(Gadget.SUGAR_RUSH, Gadget.CRASH_TEST),
            Gear.gearListWith(Gear.SUPER_CHARGE),
            List.of(StarPower.BLACK_POWDER, StarPower.WISDOM_TOOTH)
    ),
    OTIS(
            16000059, "OTIS", BrawlerRarity.MYTHIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.DORMANT_STAR, Gadget.PHAT_SPLATTER),
            Gear.gearListWith(Gear.SUPER_CHARGE),
            List.of(StarPower.STENCIL_GLUE, StarPower.INK_REFILLS)
    ),
    SAM(
            16000060, "SAM", BrawlerRarity.EPIC, BrawlerRole.ASSASSIN,
            List.of(Gadget.MAGNETIC_FIELD, Gadget.PULSE_REPELLENT),
            Gear.gearListWith(),
            List.of(StarPower.HEARTY_RECOVERY, StarPower.REMOTE_RECHARGE)
    ),
    GUS(
            16000061, "GUS", BrawlerRarity.SUPER_RARE, BrawlerRole.SUPPORT,
            List.of(Gadget.KOOKY_POPPER, Gadget.SOUL_SWITCHER),
            Gear.gearListWith(),
            List.of(StarPower.HEALTH_BONANZA, StarPower.SPIRIT_ANIMAL)
    ),
    BUSTER(
            16000062, "BUSTER", BrawlerRarity.MYTHIC, BrawlerRole.TANK,
            List.of(Gadget.UTILITY_BELT, Gadget.SLO_MO_REPLAY),
            Gear.gearListWith(),
            List.of(StarPower.BLOCKBUSTER, StarPower.KEVLAR_VEST)
    ),
    CHESTER(
            16000063, "CHESTER", BrawlerRarity.LEGENDARY, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.SPICY_DICE, Gadget.CANDY_BEANS),
            Gear.gearListWith(),
            List.of(StarPower.SINGLE_BELL_O_MANIA, StarPower.SNEAK_PEEK)
    ),
    GRAY(
            16000064, "GRAY", BrawlerRarity.MYTHIC, BrawlerRole.SUPPORT,
            List.of(Gadget.WALKING_CANE, Gadget.GRAND_PIANO),
            Gear.gearListWith(),
            List.of(StarPower.FAKE_INJURY, StarPower.NEW_PERSPECTIVE)
    ),
    MANDY(
            16000065, "MANDY", BrawlerRarity.EPIC, BrawlerRole.MARKSMAN,
            List.of(Gadget.CARAMELIZE, Gadget.COOKIE_CRUMBS),
            Gear.gearListWith(),
            List.of(StarPower.IN_MY_SIGHTS, StarPower.HARD_CANDY)
    ),
    R_T(
            16000066, "R-T", BrawlerRarity.MYTHIC, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.OUT_OF_LINE, Gadget.HACKS),
            Gear.gearListWith(),
            List.of(StarPower.QUICK_MATHS, StarPower.RECORDING)
    ),
    WILLOW(
            16000067, "WILLOW", BrawlerRarity.MYTHIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.SPELLBOUND, Gadget.DIVE),
            Gear.gearListWith(),
            List.of(StarPower.LOVE_IS_BLIND, StarPower.OBSESSION)
    ),
    MAISIE(
            16000068, "MAISIE", BrawlerRarity.EPIC, BrawlerRole.MARKSMAN,
            List.of(Gadget.DISENGAGE, Gadget.FINISH_THEM),
            Gear.gearListWith(),
            List.of(StarPower.PINPOINT_PRECISION, StarPower.TREMORS)
    ),
    HANK(
            16000069, "HANK", BrawlerRarity.EPIC, BrawlerRole.TANK,
            List.of(Gadget.WATER_BALLOONS, Gadget.BARRICADE),
            Gear.gearListWith(),
            List.of(StarPower.ITS_GONNA_BLOW, StarPower.TAKE_COVER)
    ),
    CORDELIUS(
            16000070, "CORDELIUS", BrawlerRarity.LEGENDARY, BrawlerRole.ASSASSIN,
            List.of(Gadget.REPLANTING, Gadget.POISON_MUSHROOM),
            Gear.gearListWith(),
            List.of(StarPower.COMBOSHROOMS, StarPower.MUSHROOM_KINGDOM)
    ),
    DOUG(
            16000071, "DOUG", BrawlerRarity.MYTHIC, BrawlerRole.SUPPORT,
            List.of(Gadget.DOUBLE_SAUSAGE, Gadget.EXTRA_MUSTARD),
            Gear.gearListWith(),
            List.of(StarPower.FAST_FOOD, StarPower.SELF_SERVICE)
    ),
    PEARL(
            16000072, "PEARL", BrawlerRarity.EPIC, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.OVERCOOKED, Gadget.MADE_WITH_LOVE),
            Gear.gearListWith(),
            List.of(StarPower.HEAT_RETENTION, StarPower.HEAT_SHIELD)
    ),
    CHUCK(
            16000073, "CHUCK", BrawlerRarity.MYTHIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.REROUTING, Gadget.GHOST_TRAIN),
            Gear.gearListWith(),
            List.of(StarPower.PIT_STOP, StarPower.TICKETS_PLEASE)
    ),
    CHARLIE(
            16000074, "CHARLIE", BrawlerRarity.MYTHIC, BrawlerRole.CONTROLLER,
            List.of(Gadget.SPIDERS, Gadget.PERSONAL_SPACE),
            Gear.gearListWith(),
            List.of(StarPower.DIGESTIVE, StarPower.SLIMY)
    ),
    MICO(
            16000075, "MICO", BrawlerRarity.MYTHIC, BrawlerRole.ASSASSIN,
            List.of(Gadget.CLIPPING_SCREAM, Gadget.PRESTO),
            Gear.gearListWith(),
            List.of(StarPower.MONKEY_BUSINESS, StarPower.RECORD_SMASH)
    ),
    KIT(
            16000076, "KIT", BrawlerRarity.LEGENDARY, BrawlerRole.SUPPORT,
            List.of(Gadget.CARDBOARD_BOX, Gadget.CHEESEBURGER),
            Gear.gearListWith(),
            List.of(StarPower.POWER_HUNGRY, StarPower.OVERLY_ATTACHED)
    ),
    LARRY_AND_LAWRIE(
            16000077, "LARRY & LAWRIE", BrawlerRarity.EPIC, BrawlerRole.ARTILLERY,
            List.of(Gadget.ORDER_SWAP, Gadget.ORDER_FALL_BACK),
            Gear.gearListWith(),
            List.of(StarPower.PROTOCOL_PROTECT, StarPower.PROTOCOL_ASSIST)
    ),
    MELODIE(
            16000078, "MELODIE", BrawlerRarity.MYTHIC, BrawlerRole.ASSASSIN,
            List.of(Gadget.PERFECT_PITCH, Gadget.INTERLUDE),
            Gear.gearListWith(),
            List.of(StarPower.FAST_BEATS, StarPower.EXTENDED_MIX)
    ),
    ANGELO(
            16000079, "ANGELO", BrawlerRarity.EPIC, BrawlerRole.MARKSMAN,
            List.of(Gadget.STINGING_FLIGHT, Gadget.MASTER_FLETCHER),
            Gear.gearListWith(),
            List.of(StarPower.EMPOWER, StarPower.FLOW)
    ),
    DRACO(
            16000080, "DRACO", BrawlerRarity.LEGENDARY, BrawlerRole.TANK,
            List.of(Gadget.UPPER_CUT, Gadget.LAST_STAND),
            Gear.gearListWith(),
            List.of(StarPower.EXPOSE, StarPower.SHREDDING)
    ),
    LILY(
            16000081, "LILY", BrawlerRarity.MYTHIC, BrawlerRole.ASSASSIN,
            List.of(Gadget.VANISH, Gadget.REPOT),
            Gear.gearListWith(),
            List.of(StarPower.SPIKY, StarPower.VIGILANCE)
    ),
    BERRY(
            16000082, "BERRY", BrawlerRarity.EPIC, BrawlerRole.ARTILLERY,
            List.of(Gadget.FRIENDSHIP_IS_GREAT, Gadget.HEALTHY_ADDITIVES),
            Gear.gearListWith(),
            List.of(StarPower.FLOOR_IS_FINE, StarPower.MAKING_A_MESS)
    ),
    CLANCY(
            16000083, "CLANCY", BrawlerRarity.LEGENDARY, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.SNAPPY_SHOOTING, Gadget.TACTICAL_RETREAT),
            Gear.gearListWith(),
            List.of(StarPower.RECON, StarPower.PUMPING_UP)
    ),
    MOE(
            16000084, "MOE", BrawlerRarity.LEGENDARY, BrawlerRole.DAMAGE_DEALER,
            List.of(Gadget.DODGY_DIGGING, Gadget.RAT_RACE),
            Gear.gearListWith(),
            List.of(StarPower.SKIPPING_STONES, StarPower.SPEEDING_TICKET)
    ),
    KENJI(
            16000085, "KENJI", BrawlerRarity.LEGENDARY, BrawlerRole.ASSASSIN,
            List.of(Gadget.DASHI_DASH, Gadget.HOSOMAKI_HEALING),
            Gear.gearListWith(),
            List.of(StarPower.STUDIED_THE_BLADE, StarPower.NIGIRI_NEMESIS)
    ),
    SHADE(
            16000086, "SHADE", BrawlerRarity.EPIC, BrawlerRole.ASSASSIN,
            List.of(Gadget.LONGARMS, Gadget.JUMP_SCARE),
            Gear.gearListWith(),
            List.of(StarPower.SPOOKY_SPEEDSTER, StarPower.HARDENED_HOODIE)
    ),
    JUJU(
            16000087, "JUJU", BrawlerRarity.MYTHIC, BrawlerRole.ARTILLERY,
            List.of(Gadget.VOODOO_CHILE, Gadget.ELEMENTALIST),
            Gear.gearListWith(),
            List.of(StarPower.GUARDED_GRIS_GRIS, StarPower.GUARDED_GRIS_GRIS)
    ),
    ;

    private static final Map<Long, Brawler> ENUM_BY_ID = Arrays.stream(Brawler.values())
            .filter(brawler -> brawler != UNKNOWN)
            .collect(Collectors.toMap(Brawler::getBrawlStarsId, Function.identity()));

    public static boolean exists(long brawlStarsId) {
        return ENUM_BY_ID.containsKey(brawlStarsId);
    }

    public static Brawler find(long brawlStarsId) {
        return ENUM_BY_ID.getOrDefault(brawlStarsId, UNKNOWN);
    }

    private final long brawlStarsId;
    private final String brawlStarsName;
    private final BrawlerRarity rarity;
    private final BrawlerRole role;
    private final List<Gadget> gadgets;
    private final List<Gear> gears;
    private final List<StarPower> starPowers;

    Brawler(
            long brawlStarsId, String brawlStarsName, BrawlerRarity rarity, BrawlerRole role,
            List<Gadget> gadgets, List<Gear> gears, List<StarPower> starPowers
    ) {
        this.brawlStarsId = brawlStarsId;
        this.brawlStarsName = brawlStarsName;
        this.rarity = rarity;
        this.role = role;
        this.gadgets = Collections.unmodifiableList(gadgets);
        this.gears = Collections.unmodifiableList(gears);
        this.starPowers = Collections.unmodifiableList(starPowers);
    }

    public BrawlerRole getRole() {
        return role;
    }

    public BrawlerRarity getRarity() {
        return rarity;
    }

    public String getBrawlStarsName() {
        return brawlStarsName;
    }

    public long getBrawlStarsId() {
        return brawlStarsId;
    }

    public List<Gadget> getGadgets() {
        return gadgets;
    }

    public List<Gear> getGears() {
        return gears;
    }

    public List<StarPower> getStarPowers() {
        return starPowers;
    }
}
