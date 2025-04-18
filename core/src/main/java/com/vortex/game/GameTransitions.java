package com.vortex.game;

import WorldTransitions.Aetheris_planetTransition;
import WorldTransitions.Nyxarion_planetTransition;
import WorldTransitions.Xyberion_planetTransition;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Preferences;
import com.vortex.CharacterStats.DisplayCharacters;
import com.vortex.SFX.PlayAudio;
import com.vortex.game.BattleClasses.*;

public class GameTransitions extends Game {
    private static boolean introPlayed = false;
    private int sequenceCount = 0;
    private Screen currentScreen;
    private Preferences prefs;
    private PlayAudio audioManager;
    private String NovaTextColor = "#5EABF7";
    private String WhiteText = "#FFFFFF";
    private String VioletText = "#8F00FF"; //UMBRA
    private String OrangeText = "#FFA500"; // JINA
    private String RedText = "#FF0000"; //ENEMIES

    // Volume settings
    private float musicVolume = 1.0f;
    private float soundVolume = 1.0f;

    @Override
    public void create() {
        prefs = com.badlogic.gdx.Gdx.app.getPreferences("game_settings");
        audioManager = new PlayAudio();
        loadSettings();

        if (!introPlayed) {
            introPlayed = true;
            this.setScreen(new VideoIntro(this));
        } else {
            this.setScreen(new GameMenu(this));
            //this.setScreen(new Nyxarion_planetTransition(this));

        }
    }

    public void loadSettings() {
        musicVolume = prefs.getFloat("musicVolume", 1.0f);
        soundVolume = prefs.getFloat("soundVolume", 1.0f);
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        prefs.putFloat("musicVolume", volume);
        prefs.flush();
        audioManager.setMusicVolume(volume); // Propagate to audio system
    }

    public void setSoundVolume(float volume) {
        this.soundVolume = volume;
        prefs.putFloat("soundVolume", volume);
        prefs.flush();
        audioManager.setSoundVolume(volume); // Propagate to audio system
    }
    public PlayAudio getAudioManager() {
        return audioManager;
    }

    public void startGameMenu() {
        this.setScreen(new GameMenu(this));
    }

    public void newGame() {
        sequenceCount = 0;
        startNextSequence();
        audioManager.stopAudio();
    }

    public void startNextSequence() {
        sequenceCount++;


        if(sequenceCount == 1) {
            currentScreen = new StoryScene(this, new String[]{
                "", "In the dimly lit lab, surrounded by blinking screens and the soft hum of machinery, Nova stood before her latest creation-a sleek, metallic device designed to bridge the gaps between alternate realities.", "Sequence1_1", WhiteText,
                "", "Her heart raced with excitement and trepidation. This was the moment she had spent years preparing for-", "Sequence1_1", WhiteText,
                "", "And now, she was on the brink of discovering the secrets of the multiverse.", "Sequence1_2", WhiteText,
                "Nova", "Alright, Nova, this is it.", "Sequence1_2", NovaTextColor,
                "Nova", "The moment you've been waiting for.", "Sequence1_2", NovaTextColor,
                "Nova", "Just a few more adjustments, and we'll unlock the secrets of the multiverse.", "Sequence1_2", NovaTextColor,
                "", "She adjusted the settings, her fingers dancing over the controls as she spoke to herself, seeking reassurance.", "Sequence1_3", WhiteText,
                "Nova", "Come on, Nova.", "Sequence1_3", NovaTextColor,
                "Nova", "You've studied for this.", "Sequence1_4", NovaTextColor,
                "Nova", "Just focus on the calculations.", "Sequence1_4", NovaTextColor,
                "Nova", "You've got this!", "Sequence1_4", NovaTextColor,
                "", "But as she cranked up the energy output, a warning light flashed and alarms blared, cutting through her concentration.", "Sequence1_5", WhiteText,
                "Nova", "Wha-", "Sequence1_5", NovaTextColor,
                "Nova", "No... no!", "Sequence1_5", NovaTextColor,
                "Nova", "This can't be happening!", "Sequence1_5", NovaTextColor,
                "Nova", "Not now!", "Sequence1_5", NovaTextColor,
                "", "Frantically, she scanned the screens with her mind racing.", "Sequence1_6", WhiteText,
                "Nova", "The energy levels are spiking! I need to redirect power--quick!", "Sequence1_6", NovaTextColor,
                "", "As she made adjustments, her voice trembled with fear and determination.", "Sequence1_6", WhiteText,
                "Nova", "Stay calm, stay calm... ", "Sequence1_6", NovaTextColor,
                "Nova", "You've faced worse before. Just... a little more fine-tuning", "Sequence1_6", NovaTextColor,
                "", "Suddenly, the device let out a low hum, growing louder and more chaotic.", "Sequence1_6", WhiteText,
                "Nova", "Wha... What are you doing!?", "Sequence1_6", NovaTextColor,
                "Nova", "No, no! Don't overload!", "Sequence1_6", NovaTextColor,
                "", "With a final desperate attempt, she shouted at the machine.", "Sequence1_6", WhiteText,
                "", "... As if it could hear her..", "Sequence1_6", WhiteText,
                "Nova", "AAAAAAHHHHHH!! Can't let you fail now!", "Sequence1_7", NovaTextColor,
                "Nova", "Not after everything I've worked for!", "Sequence1_7", NovaTextColor,
                "", "But before she could react, a blinding light enveloped her, and in an instant, she was torn from her lab...", "Sequence1_8", WhiteText,
                "", "... And catapulted into the unknown.", "Sequence1_9", WhiteText,
            },() -> {
                this.setScreen(new Xyberion_planetTransition(this));
            });
            setScreen(currentScreen);
        }


        //Fight Scene Begins - Nova vs. Bounty Hunters
        else if(sequenceCount == 2) {
            currentScreen = new StoryScene(this, new String[]{
                // Narration (no speaker)
                "", "Nova landed hard on a wet street bathed in neon lights.", "XYBERIA_BG", WhiteText,
                "", "She found herself sprawled on a neon-lit street, the air thick with the scent of burnt circuitry and oil.", "XYBERIA_BG", WhiteText,
                "", "Towering skyscrapers rose above her, covered with vibrant holograms that flickered erratically.", "XYBERIA_BG", WhiteText,

                // Nova's dialogue
                "Nova", "Where am I?", "NOVBK", NovaTextColor,

                // Narration
                "", "She glanced at her device, now crackling and broken in her hands, components hanging by mere threads.", "NOVHND", WhiteText,

                // Nova's dialogue
                "Nova", "Ugh! This can't be happening! I spent years perfecting this device but now it's broken! I need to find parts to fix this... but how?", "NOVHND", NovaTextColor,

                // Narration
                "", "As her thoughts raced, Nova looked around, feeling desperate yet determined.", "NOVBK", WhiteText,
                "", "She clenched her fists, refusing to give up despite the overwhelming odds against her.", "NOVBK", WhiteText,

                // Nova's dialogue
                "Nova", "There has to be something or someone around here, anything, that can help me get back to my lab! I can't give up now, not when I'm so close!", "NOVBK", NovaTextColor,

                // Narration
                "", "Suddenly, a group of cyber-enhanced bounty hunters approached, their glowing eyes scanning the area.", "CY", WhiteText,
                "", "Nova felt her heart race as they zeroed in on her, sensing her vulnerability.", "CY", WhiteText,

                // Bounty Hunter 1 (Enemy - RedText)
                "Bounty Hunter 1", "Look what we have here-a lost little girl. Hand over your tech, and we might let you go.", "CY1TK", RedText,

                // Narration
                "", "Panic surged through her. With no fighting skills, she took a deep breath and tried to think strategically.", "CY1TK", WhiteText,

                // Nova's dialogue
                "Nova", "I'm just a researcher! You don't want to hurt me. I can... I can help you with tech upgrades!", "NOVTK", NovaTextColor,

                // Narration
                "", "The hunters hesitated, but one stepped forward, a wicked grin spreading across his face.", "CYNNV", WhiteText,

                // Bounty Hunter 2 (Enemy - RedText)
                "Bounty Hunter 2", "Oh, we'll take our chances. Grab her!", "CY2", RedText
            }, () -> {
                Runnable createBattle1 = () -> {
                    currentScreen = new BattleClass(
                        this, "XYBERIA",
                        new CyberBountyHunter(),
                        false, true, false,
                        "XYBERIA_BG.png", "RoadTile.png", "XYBERIA_MUSIC.wav",
                        () -> startNextSequence()
                    );
                    setScreen(currentScreen);
                };
                createBattle1.run();
            });
            setScreen(currentScreen);
        }

        //CUTSCENE GATHERING OF MATERIALS:
        else if(sequenceCount == 3) {
            currentScreen = new StoryScene(this, new String[]{
                // Narration
                "", "Nova kneels beside one of the fallen bounty hunters, inspecting the cybernetic parts on his arm.", "XYBERIA_BG", WhiteText,
                "", "She carefully detaches a power core and a small circuit board-exactly the components she needs for her device.", "XYBERIA_BG", WhiteText,

                // Nova's dialogue (muttering to herself)
                "Nova", "Hmmm, this should work.", "XYBERIA_BG", NovaTextColor,

                // Narration
                "", "With the parts in hand, she pulled her device from her belt and carefully inserted the power core, followed by the circuit board.", "XYBERIA_BG", WhiteText,
                "", "The device sputtered, emitting a low hum as energy began to flow through it once again.", "XYBERIA_BG", WhiteText,

                // Nova's dialogue (whispering)
                "Nova", "Yes! Just a few more parts and I can get out of here..", "XYBERIA_BG", NovaTextColor,

                // Boss's dialogue (overhead, menacing)
                "Boss", "Enough!", "XYBERIA_BG", RedText,

                // Narration
                "", "From the shadows, the boss emerged-a towering figure clad in dark armor adorned with glowing circuitry, his glowing red eyes locked onto her.", "XYBERIA_BG", WhiteText,

                // Boss's dialogue
                "Boss", "What do you think you're doing? You're out of your league here.", "XYBERIA_BG", RedText,

                // Nova's dialogue (nervous)
                "Nova", "...I'll take my chances.", "XYBERIA_BG", NovaTextColor,

                // Boss's dialogue
                "Boss", "You're just a little girl playing in a world you can't comprehend. Do you think you can take on my bounty hunters? You'll regret crossing me.", "XYBERIA_BG", RedText,

                // Narration
                "", "Nova's heart raced as she stared down the towering figure before her.", "XYBERIA_BG", WhiteText,
                "", "The boss lunged forward, his massive fist swinging toward her with terrifying force.", "XYBERIA_BG", WhiteText,
                "", "Nova barely dodged, desperation gripped her-her usual kicks and punches wouldn't be enough to take him down.", "XYBERIA_BG", WhiteText,

                // Nova's dialogue (panting)
                "Nova", "I...I'm so tired. Why can't I...?", "XYBERIA_BG", NovaTextColor,

                // Narration
                "", "With her legs wobbling and exhaustion creeping into every muscle, Nova delivers a sharp elbow strike to the boss, but it barely fazes him.", "XYBERIA_BG", WhiteText,

                // Boss's dialogue
                "Boss", "Pathetic. You can't win with those weak attacks!", "XYBERIA_BG", RedText,

                // Narration
                "", "The boss retaliated with a powerful roundhouse kick, and though Nova leaped backward to avoid the blow, the fatigue was setting in fast.", "XYBERIA_BG", WhiteText,
                "", "She darted into the shadows, her eyes scanning for a weapon.", "XYBERIA_BG", WhiteText,
                "", "Spotting a bounty hunter wielding a plasma blade, she seized the moment.", "XYBERIA_BG", WhiteText,
                "", "A swift strike disarmed him, and she snatched the blade just in time to block a massive punch from the boss.", "XYBERIA_BG", WhiteText,

                // Boss's dialogue
                "Boss", "You think that will save you?", "XYBERIA_BG", RedText,

                // Narration
                "", "She struck at his side with a quick jab, but the blade barely left a mark.", "XYBERIA_BG", WhiteText,

                // Nova's dialogue (panting)
                "Nova", "Argh! I'm doomed!", "XYBERIA_BG", NovaTextColor,

                // Narration
                "", "She darted to the side, narrowly dodging another of his attacks, then ran. Not from fear, but from a growing sense of helplessness.", "XYBERIA_BG", WhiteText,
                "", "The Boss charged at her with incredible speed. He swung a metallic arm in an arc, and Nova barely ducked beneath it.", "XYBERIA_BG", WhiteText,

                // Nova's dialogue (panting)
                "Nova", "I can't... I can't fight him like this!", "XYBERIA_BG", NovaTextColor,

                // Narration
                "", "With her breath ragged, she backed away, tweaking her damaged device out of desperation.", "XYBERIA_BG", WhiteText,
                "", "Nova ducked into a corner, frantically trying to adjust the power core.", "XYBERIA_BG", WhiteText,
                "", "The device sputtered, sparks flying.", "XYBERIA_BG", WhiteText,
                "", "Nova cursed under her breath as the device malfunctioned.", "XYBERIA_BG", WhiteText,

                // Nova's dialogue (desperate)
                "Nova", "Work, dang it! I have to get out of here!", "XYBERIA_BG", NovaTextColor,

                // Narration
                "", "Suddenly, the device gave a loud hum, and a surge of energy shot through her arm.", "XYBERIA_BG", WhiteText,
                "", "She wasn't sure what she had triggered, but before she could react, the world around her shifted.", "XYBERIA_BG", WhiteText,
                "", "A blinding light engulfed her, and in an instant, she was pulled from Xyberia and flung into another universe.", "XYBERIA_BG", WhiteText
            },() -> {
                this.setScreen(new Aetheris_planetTransition(this));
            });
            setScreen(currentScreen);
        }

        // Universe 2: Aetheris - Mystical Realm
        else if(sequenceCount == 4) {
            currentScreen = new StoryScene(this, new String[]{
                // Narration
                "", "The energy surge faded, and when Nova opened her eyes, she found herself on soft moss under a swirling sky with floating islands and strange creatures.", "AT", WhiteText,

                // Nova's dialogue (breathing heavily and shaken)
                "Nova", "What... wh-where am I?", "AT", NovaTextColor,

                // Narration
                "", "As she tried to gather her bearings, she spotted a glowing piece of tech in the wreckage. She noticed a small piece of glowing tech.", "AT", WhiteText,
                "", "She picked it up-it seemed to pulse with power. It wasn't just any material-it was a key component she needed.", "AT", WhiteText,

                // Nova's dialogue (realizing)
                "Nova", "Wait... this could be it. This could give me the firepower I need!", "AT", NovaTextColor,

                // Narration
                "", "She slotted the piece into her damaged device. The makeshift gadget hummed to life once more, but this time, it was different.", "AT", WhiteText,
                "", "A small display flickered on her wrist-Energy Blaster.", "AT", WhiteText,

                // Nova's dialogue (surprised, smiling faintly)
                "Nova", "Woah...Looks like I've got an upgrade.", "AT", NovaTextColor,

                // Narration
                "", "She wiped sweat from her brow, her legs aching from the constant running and fighting from her last battle in Xyberia.", "AT", WhiteText,
                "", "Her body screamed for rest, but she knew she couldn't stop now.", "AT", WhiteText,
                "", "She clutched the Energy Blaster she recently unlocked, feeling a sliver of reassurance.", "AT", WhiteText,
                "", "She was better equipped for the challenges ahead-even if she was worn down.", "AT", WhiteText,

                // Narration
                "", "Suddenly, grotesque creatures with jagged limbs appeared out of thin air.", "AT", WhiteText,

                // Nova's dialogue (gritting her teeth, weary)
                "Nova", "Of course... more of you. Couldn't have a break, could I?", "AT", NovaTextColor,

                // Narration
                "", "She looked up, eyes widening as a massive Sky Leviathan circled overhead, casting a dark shadow over the island.", "AT", WhiteText,

                // Sky Leviathan's dialogue (booming from above)
                "Sky Leviathan", "Intruder! You dare trespass in my domain? You will pay for your arrogance!", "AT", RedText,

                // Narration
                "", "Nova stood tall despite her exhaustion. Her muscles burned, but her resolve was as unyielding as steel.", "AT2", WhiteText,

                // Nova's dialogue (defiant, with a slight smirk)
                "Nova", "Figures... A giant sky monster. Fine, let's get this over with.", "AT2", NovaTextColor,

                // Narration
                "", "With a deafening roar, the Sky Leviathan dove toward her. Winds howled in its wake, tearing at the air. Nova dove to the side, landing hard but keeping her footing.", "AT2", WhiteText,

                // Sky Leviathan's dialogue (snarling)
                "Sky Leviathan", "You won't last! I will rip you apart, mortal!", "AT2", RedText,

                // Nova's dialogue (defiant)
                "Nova", "I may be tired, but that doesn't mean I'm going down easy. Bring it on!", "AT2", NovaTextColor,

                // Sky Leviathan's dialogue
                "Sky Leviathan", "I will crush you beneath my wings, mortal! Prepare to be obliterated!", "AT2", RedText,

                // Narration
                "", "Nova felt the weight of the creature's presence, but she refused to yield. Her pulse quickened, and despite the exhaustion that weighed down on her, she steeled herself for the battle ahead.", "AT2", WhiteText,

                // Nova's dialogue
                "Nova", "Just one more fight... I-I can do this. I've come too far to quit now.", "AT2", NovaTextColor,

                // Narration
                "", "The Sky Leviathan roars and charges, its massive claws raking the ground. Nova rolls to the side, narrowly avoiding the blow, and counters with an Energy Punch to its side. The impact sends a shockwave through its scales, but the creature barely flinches.", "AT2", WhiteText,

                // Sky Leviathan's dialogue
                "Sky Leviathan", "You may have fought and survived, but no mortal can defeat the skies. I'll make sure your struggle ends in the dust of this realm. You will be forgotten.", "AT2", RedText,

                // Nova's dialogue
                "Nova", "You're right about one thing-this ends here. But it's not my struggle that'll be forgotten. It's you.", "AT2", NovaTextColor,

                // Narration
                "", "The Leviathan's eyes burned with fury at Nova's confidence. With a deafening roar, it unleashed a fierce gust of wind. Nova dodged swiftly, determination blazing in her eyes. The battle had begun.", "AT2", WhiteText
            },
                () -> {
                    Runnable createBattle1 = () -> {
                        currentScreen = new BattleClass(
                            this, "AETHERIS",
                            new SkyLeviathan(),
                            false, true, false,
                            "AETHERIS_BACKGROUND.png", "AETHERIS_TILES.png", "AETHERIS_MUSIC.wav",
                            () -> startNextSequence()
                        );
                        setScreen(currentScreen);
                    };
                    createBattle1.run();
                });
            setScreen(currentScreen);
        }

        //Key Scenes (BATTLE ALREADY ENDED HERE)
        else if(sequenceCount == 5) {
            currentScreen = new StoryScene(this, new String[]{
                // Narration
                "", "As the echoes of battle faded, a sense of calm washed over the island. Nova stood victorious, but the air still crackled with residual tension. Suddenly, a soft glow emerged from the swirling mist.", "AT3", WhiteText,

                // Narration (Umbra's entrance)
                "", "From the shadows stepped a figure cloaked in a dark, flowing robe that billowed slightly, even without a breeze. The full mask she wore was beautifully crafted, hiding the features beneath. Despite the mask, her eyes shone with a warm light, a blend of mystery and reassurance.", "AT3", WhiteText,

                // Umbra's dialogue (calm)
                "Umbra", "Impressive. Not many can defeat a Sky Leviathan on their first try.", "AT3", VioletText,

                // Narration
                "", "Startled, Nova turned to face her, a mix of wariness and curiosity flickering across her face. Despite the stormy skies, the warmth of her smile and the kindness in her gaze instantly eased Nova's tension.", "AT3", WhiteText,

                // Nova's dialogue (wary)
                "Nova", "...Who are you?", "AT3", NovaTextColor,

                // Umbra's dialogue (mysterious)
                "Umbra", "Hmmm... Umbra-Call me Umbra. Let's just say, I have my reasons for being here. And it seems we share a common goal-survival.", "AT3", VioletText,

                // Narration
                "", "Nova hesitates, her instincts telling her to be cautious, but exhaustion clouded her judgment. She watches as Umbra steps closer, her calm demeanor contrasting with the chaos surrounding them.", "AT3", WhiteText,

                // Nova's dialogue (skeptical)
                "Nova", "Survival? I'm just trying to find my way back to my world. How 'bout you? What are you after?", "AT3", NovaTextColor,

                // Umbra's dialogue (reassuring)
                "Umbra", "Perhaps I can help you with that. But you must understand, Aetheris is only the beginning. There are other worlds, and each will test you in ways you can't yet imagine.", "AT3", VioletText,

                // Narration
                "", "Nova's mind raced, but the shard of glowing tech in her hand reminded her of her broken device and the urgent need for repairs.", "AT3", WhiteText,

                // Nova's dialogue (urgent)
                "Nova", "I need to find materials to fix my device. Without it, I'm lost.", "AT3", NovaTextColor,

                // Umbra's dialogue (informative)
                "Umbra", "Then you must seek out the Nyxarion-The Shattered Wasteland. It holds remnants of ancient technology, pieces you might find essential.", "AT3", VioletText,

                // Narration
                "", "Nova's eyes widened at the mention of the Nyxarion, a place whispered about in fearful tones.", "AT3", WhiteText,

                // Nova's dialogue (curious)
                "Nova", "What kind of materials?", "AT3", NovaTextColor,

                // Umbra's dialogue (explanatory)
                "Umbra", "Elements from the ancient tech-quantum coils and energy conductors. They're scattered throughout the Nyxarion, remnants of the battles that shaped this realm.", "AT3", VioletText,

                // Narration
                "", "Nova glances at the shard in her hand, knowing she can't do this alone, but unsure if she can trust Umbra.", "AT3", WhiteText,

                // Nova's dialogue (suspicious)
                "Nova", "And you just happen to show up after the fight?", "AT3", NovaTextColor,

                // Umbra's dialogue (mysterious)
                "Umbra", "Coincidence... or perhaps fate. Either way, you'll need me for the trials ahead.", "AT3", VioletText,

                // Narration
                "", "The storm around them begins to settle, but Nova senses the tension in the air. With no other option in sight, she nods reluctantly.", "AT", WhiteText,

                // Nova's dialogue (reluctant)
                "Nova", "Fine. But if you're lying to me...", "AT", NovaTextColor,

                // Umbra's dialogue (reassuring)
                "Umbra", "I wouldn't dream of it.", "AT", VioletText,

                // Narration (portal scene)
                "", "Nova and Umbra stand before the unstable portal, its energy flickering. The air is heavy with tension.", "AT", WhiteText,

                // Nova's dialogue (observant)
                "Nova", "Looks like it could tear apart any second.", "AT", NovaTextColor,

                // Umbra's dialogue (focused)
                "Umbra", "It will if we don't act fast. Gather the crystals-too much energy, and it'll overload. Too little, and it'll collapse.", "AT", VioletText,

                // Narration
                "", "Nova gathers glowing crystals, placing them near the portal. The portal stabilizes, pulsing steadily.", "AT", WhiteText,

                // Umbra's dialogue
                "Umbra", "It's ready. Once inside, stay close. The Void distorts everything.", "AT", VioletText,

                // Nova's dialogue (determined)
                "Nova", "Let's go.", "AT", NovaTextColor,

                // Narration (Jina's appearance)
                "", "Nova and Umbra tread cautiously towards the portal, the shifting shadows warping and distorting the space around them. Out of the swirling mist, a figure appears, cloaked in the same eerie glow as the environment. A woman steps forward, her expression unreadable but her presence commanding.", "AT", WhiteText,

                // Jina's dialogue (calm)
                "Jina", "Ya're venturin' into dangerous territory, gal.", "AT", OrangeText,

                // Narration
                "", "Nova grips her weapon tighter, wary but intrigued.", "AT", WhiteText,

                // Nova's dialogue (wary)
                "Nova", "Who are you?", "AT", NovaTextColor,

                // Jina's dialogue (mysterious)
                "Jina", "Jina. And ya're walkin' straight into a trap. The bloke that controls this part is more powerful than anythin' ya've faced 'afore.", "AT", OrangeText,

                // Umbra's dialogue (cool)
                "Umbra", "We know the risks.", "AT", VioletText,

                // Narration
                "", "Jina's gaze shifts briefly to Umbra before locking back onto Nova.", "AT", WhiteText,

                // Jina's dialogue (warning)
                "Jina", "Do ya? The Nyxarion twists more'n just reality. It distorts motives, plays with ya mind. Ya'll need more'n just strength to survive 'ere.", "AT", OrangeText,

                // Nova's dialogue (suspicious)
                "Nova", "Why are you telling us this?", "AT", NovaTextColor,

                // Jina's dialogue (cryptic)
                "Jina", "'Cause not everythin' 'ere is what it seems. Be careful who ya trust.", "AT", OrangeText,

                // Narration
                "", "Nova confusingly glances at Umbra. Umbra stays silent, her face unreadable.", "AT", WhiteText,

                // Nova's dialogue (firm)
                "Nova", "If you want to help, then help. But we don't have time to play games.", "AT", NovaTextColor,

                // Jina's dialogue (nonchalant)
                "Jina", "I'm offerin' a warnin', not a game. Take it or leave it.", "AT", OrangeText,

                // Narration
                "", "The tension between the three lingers in the air, but Nova remains focused, her resolve unshaken despite the cryptic message. They press on, Jina falling into step behind them.", "AT2", WhiteText,

                // Jina's dialogue (quiet warning)
                "Jina", "Trust ya instincts. Sometimes the most dangerous things are the ones closest to ya.", "AT2", OrangeText,

                // Nova's dialogue (determined)
                "Nova", "I'm not turning back now.", "AT2", NovaTextColor,

                // Narration
                "", "The atmosphere becomes even more oppressive.", "AT2", WhiteText,

                // Nova's dialogue (questioning)
                "Nova", "You've fought something like this before?", "AT2", NovaTextColor,

                // Jina's dialogue (experienced)
                "Jina", "I've seen enough to know 'ow dangerous it is. Stay sharp. It won't attack 'til it's sure of its prey.", "AT2", OrangeText,

                // Narration (final transition)
                "", "They step into the swirling portal, the world around them warping into nothingness.", "Sequence1_8", WhiteText,

                // Narration (portal transition)
                "", "A brilliant flash engulfs them as Nova, Umbra, and Jina step through the portal. The air shifts-heavy, charged, and unsettling. As the light recedes, an eerie silence fills the space. They now stand on fractured ground, where time and reality seem to collide and shatter endlessly.", "Sequence1_8", WhiteText
            },() -> {
                this.setScreen(new Nyxarion_planetTransition(this));
            });
            setScreen(currentScreen);
        }

        //Key Scenes (BATTLE ALREADY ENDED HERE)
        else if(sequenceCount == 6) {
            currentScreen = new StoryScene(this, new String[]{
                // Nova's dialogue (observant)
                "Nova", "So... this is Nyxarion...", "NyxCenter", NovaTextColor,
                "Nova", "This place... it feels wrong.", "NyxCenter", NovaTextColor,

                // Jina's dialogue (grim)
                "Jina", "It's worse'n I remember. The echoes 'ere... they're louder.", "NyxCenter", OrangeText,

                // Narration (reality distortions)
                "", "They push forward as strange distortions appear-fractured images of other realities.", "NyxCenter", WhiteText,

                // Nova's dialogue (uneasy)
                "Nova", "What are those...?", "NyxCenter", NovaTextColor,

                // Umbra's warning (cold)
                "Umbra", "Echoes. Fractured pieces of other dimensions. Move quickly before they pull us in.", "NyxCenter", VioletText,

                // Narration (visions)
                "", "A distorted image flashes-a ruined city where Nova fights alone, her body bloodied but her expression hardened. Another shows Umbra standing amidst a scorched battlefield, her face shadowed and unreadable. The visions vanish as quickly as they appear, but the unease lingers in Nova's mind.", "NyxCity", WhiteText,

                // Nova's reaction
                "Nova", "They feel... too real.", "NyxCity", NovaTextColor,

                // Jina's explanation
                "Jina", "'Cause they are. Nyxarion don't just warp space-it bends ya perception, feeds on doubt. And if ya lose focus...", "NyxCity", OrangeText,

                // Umbra's ominous line
                "Umbra", "It devours you.", "NyxCity", VioletText,

                // Narration (environmental tension)
                "", "They push forward, the ground beneath them vibrating with an unsettling hum. Each step feels heavier, the air thickening as an oppressive force presses down on them. Shadows shift unnervingly, forming distorted shapes that vanish as soon as they're noticed.", "NyxDark", WhiteText,

                // Nova's alert
                "Nova", "Whatever's out there... it's watching us.", "NyxDark", NovaTextColor,

                // Jina's foreboding reply
                "Jina", "Not just watchin'. Waitin'.", "NyxDark", OrangeText,

                // Narration (Void Sentinels appear)
                "", "A guttural rumble echoes through the air. From the distance, movement stirs-a shiver through the shadows. Figures begin to emerge, their outlines jagged and flickering. The Void Sentinels-tall, faceless beings formed from broken fragments of reality-materialize, their hollow forms pulsing with malevolent energy.", "NyxDark", WhiteText,
                "", "As the air thickens, an unnatural chill fills the space. From the shifting darkness, towering figures emerge, their outlines jagged and flickering. The Void Sentinels-void-black and unnervingly still. Their forms are humanoid but hollow, with eyes that burn like dying stars.", "NyxDark", WhiteText,

                // Umbra's alert
                "Umbra", "We're not alone anymore.", "NyxDark", VioletText,

                // Jina's ominous warning
                "Jina", "And this... is just the beginnin'.", "NyxDark", OrangeText,

                // Narration (battle prep)
                "", "The Sentinels advance, their presence distorting the air around them. Each step sends out ripples of dark energy that twist the surroundings. Nova steadies her breath, her pulse pounding in her ears.", "NyxDark", WhiteText,

                // Nova's resolve
                "Nova", "If we fight, we fight smart. No mistakes.", "NyxDark", NovaTextColor,

                // Umbra's tactical advice
                "Umbra", "Agreed. But be ready... these things don't fight fair.", "NyxNeon", VioletText,

                // Narration (Jina's behavior)
                "", "Jina stays close, her posture tense but composed. Her eyes flick between Nova and Umbra, her expression unreadable-yet something about her seems... distant, as though her thoughts are elsewhere.", "NyxNeon", WhiteText,

                // Jina's cryptic warning
                "Jina", "Keep ya guard up. Nyxarion bends more'n reality. It bends trust.", "NyxNeon", OrangeText,

                // Narration (Nova's doubt)
                "", "Nova's eyes briefly meet Umbra's, a flicker of uncertainty crossing her mind, but she pushes it aside. There's no room for doubt now.", "NyxNeon", WhiteText,

                // Nova's rally
                "Nova", "We end this. Together.", "NyxNeon", NovaTextColor,

                // Final narration (cliffhanger)
                "", "As the shadows close in, the three move in unison-ready to confront whatever horrors Nyxarion throws their way. But beneath the chaos, a darker threat lingers... one that's been by Nova's side all along.", "NyxNeon", WhiteText
            },
                () -> {
                    Runnable createBattle1 = () -> {
                        currentScreen = new BattleClass(
                            this, "NYXARION",
                            new VoidSentinel(),
                            true, true, true,
                            "NYXARION_BACKGROUND.png", "NYXARION_TILE.png", "NYXARION_MUSIC.wav",
                            () -> startNextSequence()
                        );
                        setScreen(currentScreen);
                    };
                    createBattle1.run();
                });
            setScreen(currentScreen);
        }

        //THE BATTLE ENDED
        else if(sequenceCount == 7) {
            currentScreen = new StoryScene(this, new String[]{
                // Narration
                "", "The remains of the Void Sentinels dissolve into fragments of shadow, vanishing into the corrupted ground beneath them. Nova, Umbra, and Jina stand amidst the aftermath, their breaths steadying as the unsettling silence returns. But the air is heavier now-charged, as if the battle has disturbed something deeper within Nyxarion.", "NyxNeon", WhiteText,

                // Nova's dialogue (wiping her blade, her voice low)
                "Nova", "That was too close...", "NyxNeon", NovaTextColor,

                // Jina's dialogue (nodding, eyes still scanning the horizon)
                "Jina", "And it ain't over. This realm don't stay quiet for long.", "NyxNeon", OrangeText,

                // Umbra's dialogue (calm, almost too composed)
                "Umbra", "We need to move. The longer we stay, the more this place twists reality around us.", "NyxNeon", VioletText,

                // Nova's dialogue (narrowing her eyes, her instincts stirring)
                "Nova", "And where exactly are we going now, Umbra?", "NyxNeon", NovaTextColor,

                // Narration
                "", "(Umbra's gaze meets Nova's.)", "NyxNeon", WhiteText,

                // Umbra's dialogue (voice steady)
                "Umbra", "To the core of this realm. Where the energy fractures are strongest. I told you... there's something there. A material powerful enough to amplify your device beyond its limits.", "NyxLightning", VioletText,

                // Jina's dialogue (softly, but with an edge of suspicion)
                "Jina", "And ya sure it's safe, Purple?", "NyxLightning", OrangeText,

                // Umbra's dialogue (a slight, knowing smile)
                "Umbra", "Nothing in Nyxarion is safe. But power doesn't come without risk, does it?", "NyxLightning", VioletText,

                // Narration
                "", "(They press forward, the oppressive air growing thicker with each step. The landscape wraps around them-twisted remnants of broken realities, whispers of forgotten worlds echoing in the wind. But as they move deeper, Nova feels something... different. Her device-strapped tightly to her wrist-begins to hum, faintly resonating with the strange energy that pulses through the ground.)", "NyxUnder", WhiteText,

                // Nova's dialogue (frowning, glancing at her device)
                "Nova", "This... it's reacting to something.", "NyxUnder", NovaTextColor,

                // Jina's dialogue (noticing, her voice tinged with caution)
                "Jina", "That ain't normal... what's it pickin' up?", "NyxUnder", OrangeText,

                // Umbra's dialogue (watching closely, her tone neutral but her eyes calculating)
                "Umbra", "Perhaps... we're getting close.", "NyxUnder", VioletText,

                // Narration
                "", "(They continue deeper into the wasteland, the ground beneath them growing unstable, pulsing faintly with raw energy. As they approach a narrow ravine, a sudden tremor shakes the ground violently. Nova steadies herself, but then... the air shifts-charged, alive.)", "NyxUnder", WhiteText,

                // Jina's dialogue (eyes narrowing)
                "Jina", "Somethin's comin'.", "NyxUnder", OrangeText,

                // Narration
                "", "(A deafening roar echoes from the depths below. The ground cracks open, and from the abyss, a monstrous figure bursts forth-an Energy Wyrm. Its serpentine body is a swirling mass of unstable energy, with arcs of raw power crackling along its iridescent scales. Its eyes burn with a chaotic light, and its movements leave trails of electric distortion in the air.)", "NyxUnder", WhiteText,

                // Nova's dialogue (eyes widening)
                "Nova", "What... is that?!", "NyxUnder", NovaTextColor,

                // Umbra's dialogue (calm but with a dangerous glint in her eyes)
                "Umbra", "The guardian of Nyxarion's core.", "NyxUnder", VioletText,

                // Jina's dialogue (voice urgent)
                "Jina", "It's pure energy! We can't fight it head-on!", "NyxUnder", OrangeText,

                // Nova's dialogue (gritting her teeth, her stance shifting as she readies her weapon)
                "Nova", "We don't have a choice. If that thing's guarding the core... we need to get past it.", "NyxUnder", NovaTextColor,

                // Jina's dialogue (voice firm, determination replacing hesitation)
                "Jina", "Then we hit it hard 'n fast. We bring it down 'fore it drains us.", "NyxUnder", OrangeText,

                // Umbra's dialogue (stepping forward, her tone cool but laced with something unreadable)
                "Umbra", "Agreed. But don't underestimate it. Creatures born of pure energy don't go down easily.", "NyxUnder", VioletText,

                // Nova's dialogue (eyes narrowing, her grip tightening)
                "Nova", "Then we better make every move count.", "NyxUnder", NovaTextColor,

                // Narration
                "", "(The air crackles with tension as Nova and Umbra stand facing each other, the ruins of Nyxarion trembling under the weight of their clashing energies. The dark crystal pulses violently between them, its power unstable-a conduit for the abyss that Umbra seeks to unleash. Jina stands at Nova's side, her weapon drawn, her eyes locked onto Umbra with unshaken defiance.)", "NyxUnder", WhiteText
            },
                () -> {
                    Runnable createBattle1 = () -> {
                        currentScreen = new BattleClass(
                            this, "NYXARION",
                            new EnergyWyrm(),
                            true, true, true,
                            "NYXARION_BACKGROUND.png", "NYXARION_TILE.png", "NYXARION_MUSIC.wav",
                            () -> startNextSequence()
                        );
                        setScreen(currentScreen);
                    };
                    createBattle1.run();
                });
            setScreen(currentScreen);
        }

        //FINAL SEQUENCE NIGGAREVELATIONS
        else if(sequenceCount == 8) {
            currentScreen = new StoryScene(this, new String[]{
                // Narration
                "", "(The battlefield is silent now, save for the faint crackle of dissipating energy. The shattered remains of the Energy Wyrm lie motionless, its once-vibrant core now dimming, releasing waves of unstable power into the air. Nova, Jina, and Umbra stand amidst the aftermath-wary but victorious.)", "NyxUnder", WhiteText,

                // Nova's dialogue (catching her breath, her weapon lowering)
                "Nova", "That... was intense.", "NyxUnder", NovaTextColor,

                // Jina's dialogue (exhaling, her tone relieved but cautious)
                "Jina", "We're lucky to be standin'. That thing was more'n just raw energy.", "NyxUnder", OrangeText,

                // Umbra's dialogue (calm, her gaze fixed on the fading remnants of the Wyrm)
                "Umbra", "It wasn't just a creature. It was a conduit... holding the energy that sustains Nyxarion's core.", "NyxUnder", VioletText,

                // Narration
                "", "(As the energy dissipates, Nova's device-still strapped securely to her wrist-begins to pulse faintly. The hum is different this time... deeper, more resonant. It's as if the remnants of the Wyrm's power are being drawn toward it.)", "NyxUnder", WhiteText,

                // Nova's dialogue (frowning, glancing down at her device)
                "Nova", "Wait... something's happening.", "NyxUnder", NovaTextColor,

                // Narration
                "", "(The device pulses faster, the energy surrounding them slowly converging. Thin tendrils of light weave toward Nova's device, drawn to it like moths to a flame.)", "NyxSwirl", WhiteText,

                // Jina's dialogue (eyes widening)
                "Jina", "Nova... ya device...", "NyxSwirl", OrangeText,

                // Narration
                "", "(The core of the defeated Wyrm flickers one last time before releasing a final surge of energy-a radiant burst that spirals toward Nova. Instinctively, she raises her arm to shield herself, but the energy doesn't attack... it converges.)", "NyxSwirl", WhiteText,

                // Nova's dialogue (voice tense)
                "Nova", "It's... merging with my device?", "NyxSwirl", NovaTextColor,

                // Narration
                "", "(The energy pulses violently as it's absorbed into Nova's device, causing it to glow brighter than ever. The interface flickers, struggling to contain the sheer force being channeled through it. The hum grows louder, resonating not just through the device-but through Nova herself.)", "NyxSwirl", WhiteText,

                // Jina's dialogue (stepping closer, her voice a mix of awe and concern)
                "Jina", "It ain't just energy... it's reshapin' ya device. It's... evolvin' it.", "NyxSwirl", OrangeText,

                // Umbra's dialogue (watching closely, her expression unreadable)
                "Umbra", "This power... it's adapting to her.", "NyxSwirl", VioletText,

                // Narration
                "", "(Nova grits her teeth as the energy surges through her, her mind flooded with fragmented images-different realities, timelines overlapping for a split second. The weight of infinite possibilities presses against her consciousness, but her body... adjusts. The chaos stabilizes, and in that moment, everything changes.)", "NyxSwirl", WhiteText,

                // Nova's dialogue (voice echoing softly, eyes glowing faintly)
                "Nova", "I can... feel it. I can see... everything.", "NyxSwirl", NovaTextColor,

                // Narration
                "", "(The device locks into place, its exterior now humming with a powerful glow. A new interface flickers across the surface, displaying incomprehensible coordinates and energy signatures. Nova's breath steadies as the realization hits her-she's unlocked something beyond her previous limits.)", "NyxSwirl", WhiteText,

                // Nova's dialogue (still surprised but overwhelmed with awe)
                "Nova", "I just unlocked my ultimate skill... Multidimensional Blast!", "NyxSwirl", NovaTextColor,

                // Jina's dialogue (grinning, her eyes shining with excitement)
                "Jina", "Nova... ya did it. That's... an entirely new level o' power.", "NyxSwirl", OrangeText,

                // Nova's dialogue (eyes narrowing, her voice steady but pulsing with newfound power)
                "Nova", "This... this is more than just an upgrade. I'm closer than ever to achieving what I've been fighting for.", "NyxSwirl", NovaTextColor,

                // Jina's dialogue (beaming, her voice overflowing with hope)
                "Jina", "This changes everythin'. With this, we can stop whatever Nyxarion throws at us. No more bein' one step behind.", "NyxSwirl", OrangeText,

                // Umbra's dialogue (eyes narrowing slightly, her tone smooth but laced with subtle intent)
                "Umbra", "Not really if you're facing a more powerful foe than the Wyrm. The energy's powerful... but unstable. You'll need something more to fully control it.", "NyxNeon", VioletText,

                // Nova's dialogue (voice firm, eyes burning with resolve)
                "Nova", "I know this isn't enough. There's more out there-something crucial to stabilize this power. But where do we even begin?", "NyxNeon", NovaTextColor,

                // Narration
                "", "(Umbra's eyes gleam with calculated understanding, a plan quietly taking shape in her mind. She steps forward, her tone calm yet persuasive.)", "NyxNeon", WhiteText,

                // Umbra's dialogue (softly, but with a hint of intrigue)
                "Umbra", "I may know where to start. An ancient site... hidden deep within the core of Nyxarion. It holds untapped energy-something that might be exactly what you need.", "NyxNeon", VioletText,

                // Nova's dialogue (carefully)
                "Nova", "And you're certain it's there?", "NyxNeon", NovaTextColor,

                // Umbra's dialogue (nodding, her expression serious but her motives carefully masked)
                "Umbra", "I can lead you there. But we need to act quickly-before Nyxarion shifts again.", "NyxNeon", VioletText,

                // Narration
                "", "(Nova exchanges a glance with Jina, who remains quiet but watches Umbra closely, her instincts whispering caution.)", "NyxNeon", WhiteText,

                // Nova's dialogue (after a beat, her voice steady)
                "Nova", "Alright. Show me.", "NyxNeon", NovaTextColor,

                // Narration
                "", "(Umbra approaches, her fingers moving deftly over Nova's device. She inputs the coordinates with practiced precision, the device humming softly as it processes the new data. A soft glow pulses from the screen-a beacon guiding them deeper into the unknown.)", "NyxNeon", WhiteText,

                // Jina's dialogue (voice calm but guarded)
                "Jina", "Let's 'ope this leads us to what we're lookin' for.", "NyxNeon", OrangeText,

                // Nova's dialogue (eyes set ahead, her voice steady)
                "Nova", "I'm not backing down. Whatever's waiting for us... we face it head-on.", "NyxNeon", NovaTextColor,

                // Narration
                "", "(They share a moment of silent understanding, an unbreakable bond forming as they prepare to face whatever challenges lie ahead. Together, they step into the unknown, ready to reclaim their power.)", "NyxNeon", WhiteText,

                // Narration
                "", "(Their footsteps echo softly against the cracked earth as they venture deeper into the heart of Nyxarion. A chilling wind brushes past, carrying whispers of a forgotten past. Shadows stretch across the ruins, jagged remnants of once-great structures rising like broken sentinels. Each step draws them closer to the core of this desolate world, where answers-and danger-await in equal measure.)", "NyxCity", WhiteText,

                // Narration
                "", "(Nova stands in stunned silence, her eyes sweeping across the shattered landscape. Beside her, Jina remains tense, scanning for signs of movement. Umbra observes them, a faint, unreadable smile on her face.)", "NyxCity", WhiteText,

                // Nova's dialogue (voice barely above a whisper, her tone filled with unease)
                "Nova", "What happened here?", "NyxCity", NovaTextColor,

                // Jina's dialogue (tense, scanning their surroundings)
                "Jina", "This place... it's nothin' but ruins. Ya sure there's anythin' left that could 'elp us, Purple?", "NyxCity", OrangeText,

                // Narration
                "", "(Umbra steps forward, her movements graceful but deliberate. The air seems to shift around her, as if responding to her presence.)", "NyxCity", WhiteText,

                // Umbra's dialogue (calm, her tone laced with a distant sorrow)
                "Umbra", "What you need is here, Nova. The energy in this realm... it's broken, yes, but broken things have a way of becoming something far more potent. This place still breathes... it just hides its strength.", "NyxCity", VioletText,

                // Nova's dialogue (voice low, suspicion growing)
                "Nova", "You speak like you know this world too well... Umbra. Just what exactly happened here?", "NyxCity", NovaTextColor,

                // Narration
                "", "(Umbra's expression shifts, her eyes momentarily distant as if recalling a painful memory. But the flicker of emotion is gone as quickly as it appeared.)", "NyxCity", WhiteText,

                // Umbra's dialogue (barely a whisper, her words carefully chosen)
                "Umbra", "What happened here was... ambition. To unlock the secrets of multiversal power. But... ambition often demands sacrifice.", "NyxCity", VioletText,

                // Jina's dialogue (voice edged with suspicion)
                "Jina", "Ya keep talkin' like ya know more'n ya're tellin' us. 'Ow do we know this ain't leadin' us straight into a trap?", "NyxCity", OrangeText,

                // Narration
                "", "(Umbra meets Jina's gaze, her expression unreadable, but there's a flicker of amusement-so faint it's almost imperceptible.)", "NyxCity", WhiteText,

                // Umbra's dialogue (smiling slightly, her tone carrying a cryptic edge)
                "Umbra", "A trap? Perhaps... but power is rarely given without a price. Consider it... an opportunity. One that only the worthy can claim.", "NyxCity", VioletText,

                // Nova's dialogue (eyes narrowing, her resolve hardening despite the growing unease)
                "Nova", "I've come too far to stop now. If what we need is here, then we press on. Lead the way, Umbra.", "NyxCity", NovaTextColor,

                // Narration
                "", "(Umbra guides them deeper into the ruins, her movements fluid and sure, as though she's walked this path countless times before. The air thickens with an oppressive stillness, the silence pressing down like a weight. The landscape is haunting-fractured remnants of ancient structures stretching toward the sky, their jagged edges whispering of forgotten knowledge and devastating power.)", "NyxCity", WhiteText,

                // Narration
                "", "(Nova's device hums softly, reacting to the latent energy that pulses faintly beneath the ground. The air is charged... alive.)", "NyxCity", WhiteText,

                // Nova's dialogue (frowning, glancing down at her device)
                "Nova", "It's... reacting again. Whatever's here... it's close.", "NyxCity", NovaTextColor,

                // Jina's dialogue (noticing, her voice filled with quiet concern)
                "Jina", "That ain't normal... it's like the energy is... guidin' us.", "NyxCity", OrangeText,

                // Narration
                "", "(Umbra's expression remains calm, but her eyes betray a glimmer of something deeper-satisfaction. She walks ahead, her fingers brushing against the crumbling walls as though she's absorbing the very essence of the ruins.)", "NyxCity", WhiteText,

                // Umbra's dialogue (soft, almost to herself)
                "Umbra", "The core remembers... it calls to those who understand its true potential.", "NyxCity", VioletText,

                // Nova's dialogue (suspicious, her tone firm)
                "Nova", "Umbra... how do you know so much about this place?", "NyxCity", NovaTextColor,

                // Narration
                "", "(Umbra pauses, her back to them, her posture perfectly still.)", "NyxCity", WhiteText,

                // Umbra's dialogue (voice low, carefully measured)
                "Umbra", "Because I've... studied what came before. And I know that power like this... doesn't simply vanish. It lingers... waiting for someone strong enough to claim it.", "NyxCity", VioletText,

                // Narration
                "", "(Nova and Jina exchange a glance, unease thickening between them. Jina's gaze sharpens, her instincts screaming that something is wrong.)", "NyxCity", WhiteText,

                // Narration
                "", "(They finally arrive at the heart of the ruins-a vast, hollowed-out chamber pulsating with ancient energy. The walls shimmer faintly, etched with glowing runes that pulse in rhythm with the core's latent power. At the center lies a pedestal, cradling a crystalline fragment-a dark crystal swirling with raw, uncontained energy, its surface radiating both brilliance and corruption. The air vibrates with anticipation, as if the realm itself is watching.)", "NyxLightning", WhiteText,

                // Narration
                "", "(Nova's device hums louder, resonating in perfect harmony with the crystal as if they were two halves of the same force. The pull is undeniable, drawing her closer with every heartbeat.)", "NyxLightning", WhiteText,

                // Nova's dialogue (voice barely above a whisper, awe and uncertainty intertwining)
                "Nova", "This... this is it. The power I need to fix my device. I can feel it.", "NyxLightning", NovaTextColor,

                // Jina's dialogue (voice laced with concern, her eyes locked on the pulsating crystal)
                "Jina", "Nova... somethin' don't feel right. This energy... it's too unstable.", "NyxLightning", OrangeText,

                // Umbra's dialogue (soft, her voice laced with an unsettling calm)
                "Umbra", "What are you waiting for, Nova? This is what you've been searching for. Don't let doubt cloud your judgment.", "NyxLightning", VioletText,

                // Narration
                "", "(Nova hesitates, her fingers hovering just above the crystal, her instincts screaming caution. But the hum of her device grows louder, almost... eager.)", "NyxLightning", WhiteText,

                // Nova's dialogue (grinning, her voice filled with exhilaration)
                "Nova", "It worked! The power... I can feel it! This changes everything!", "NyxLightning", NovaTextColor,

                // Narration
                "", "(But before the glow fully fades, the atmosphere in the room shifts. The light dims as a chilling stillness fills the air. The energy from the crystal lingers, but it feels... different now. Darker. Almost... alive.)", "NyxSwirl", WhiteText,

                // Umbra's dialogue (stepping forward, her tone no longer filled with quiet encouragement but dripping with intent)
                "Umbra", "Congratulations, Nova. But did you really think I'd let you walk away with that power?", "NyxSwirl", VioletText,

                // Nova's dialogue (confused)
                "Nova", "Wha-What do you mean, Umbra? I-I don't understand you.", "NyxSwirl", NovaTextColor,

                // Umbra's dialogue (smirking, her voice echoing with malice)
                "Umbra", "You've done exactly what I needed, Nova. You've unlocked the power I've been seeking... and now, it's time for you to hand it over.", "NyxSwirl", VioletText,

                // Nova's dialogue (eyes narrowing, her guard rising)
                "Nova", "Umbra... what are you talking about?", "NyxSwirl", NovaTextColor,

                // Umbra's dialogue (distant, voice tinged with sorrow)
                "Umbra", "This place was built by someone like you, Nova-someone who defied limits, seeking multiversal power... only to be left in the ruins of shattered dreams and unrelenting ambition.", "NyxSwirl", VioletText,

                // Narration
                "", "(Nova takes a step back, the realization dawning on her, but she struggles to accept it. Her eyes widen, searching for answers in the fragments around them.)", "NyxSwirl", WhiteText,

                // Nova's dialogue (voice quivering, horror seeping in)
                "Nova", "This world... Umbra, you knew it because... you're from here, aren't you?", "NyxSwirl", NovaTextColor,

                // Narration
                "", "(Umbra pauses, her shoulders slumping as though the weight of her past is finally too much to bear. She reaches up and, with a slow, deliberate movement, removes her mask. Beneath it is a face that mirrors Nova's but is etched with the scars of battle and despair, eyes darkened by loss and regret. She holds Nova's gaze, allowing her to see the truth.)", "NyxSwirl", WhiteText,

                // Umbra's dialogue (quietly, resigned, her voice a mixture of pain and anger)
                "Umbra", "Yes, Nova. I am... or rather, I was you. I am the version of you who sacrificed everything, who gambled with life and worlds to try and create something greater. I failed, Nova. Every experiment, every risk... all of it fell to ruin. This-", "NyxSwirl", VioletText,

                // Narration
                "", "(She gestures to the wasteland around them.)", "NyxSwirl", WhiteText,

                // Umbra's dialogue (continuing)
                "Umbra", "-is the price of ambition unbridled.", "NyxSwirl", VioletText,

                // Nova's dialogue (whispering, horror etched on her face)
                "Nova", "You can't just take what's mine! I've fought too hard, bled too much, and sacrificed everything to get here. If you couldn't find a way to fix your mistakes... then that's on you. But stealing my journey, my hope... my future? That's not redemption, Umbra. It's just another failure-and I won't let you make it mine.", "NyxSwirl", NovaTextColor,

                // Umbra's dialogue (her voice dark, with a twisted sense of triumph)
                "Umbra", "You don't understand, Nova. I didn't just bring you here-I created this chance, this world, because I need what you have. Your victories, your strength, every skill you've fought to gain... I need it all to open the door to greater dark energies, to fix my past failures.", "NyxSwirl", VioletText,

                // Narration
                "", "(Nova, caught in the tide of betrayal, feels the weight of Umbra's pain as well as the danger it poses.)", "NyxSwirl", WhiteText,

                // Umbra's dialogue (her expression darkening, a bitter smile tugging at her lips)
                "Umbra", "This isn't about redemption... it's about survival.", "NyxSwirl", VioletText,

                // Narration
                "", "(She takes a step closer, her voice dropping to a dangerous whisper.)", "NyxSwirl", WhiteText,

                // Umbra's dialogue (continuing)
                "Umbra", "I've already lost everything once, Nova. I watched my world crumble, my dreams shatter... and I was powerless to stop it. But not this time.", "NyxSwirl", VioletText,

                // Jina's dialogue (eyes locked on Umbra, her stance firm)
                "Jina", "Nova ain't alone in this. Ya might 'ave 'er face, Purple... but ya'll never 'ave 'er strength. Nova didn't need darkness to become powerful-she forged 'er own path, and that's somethin' ya'll never understand.", "NyxSwirl", OrangeText,

                // Narration
                "", "(Her fingers tighten around her weapon, her resolve echoing Nova's unyielding spirit.)", "NyxSwirl", WhiteText,

                // Jina's dialogue (voice sharper, challenging)
                "Jina", "Ya'll 'ave to go through me first.", "NyxSwirl", OrangeText,

                // Narration
                "", "(Umbra's smirk fades for a brief moment, her eyes flickering with something unreadable-regret? Or just a fleeting glimpse of doubt?)", "NyxSwirl", WhiteText,

                // Umbra's dialogue (her expression hardening, her voice dripping with malice again)
                "Umbra", "Then so be it. But remember this-", "NyxSwirl", VioletText,

                // Narration
                "", "(She steps forward, the shadows around her writhing as if alive.)", "NyxSwirl", WhiteText,

                // Umbra's dialogue (cold and unforgiving)
                "Umbra", "When the Abyssal Dominion opens... mercy won't be an option.", "NyxSwirl", VioletText,

                // Nova's dialogue (voice steady but fierce, her fists crackling with energy)
                "Nova", "I'm not asking for mercy, Umbra. I'm here to end this-once and for all.", "NyxSwirl", NovaTextColor,

                // Narration
                "", "(The air crackles with tension as Nova and Umbra stand facing each other, the ruins of Nyxarion trembling under the weight of their clashing energies. The dark crystal pulses violently between them, its power unstable-a conduit for the abyss that Umbra seeks to unleash. Jina stands at Nova's side, her weapon drawn, her eyes locked onto Umbra with unshaken defiance.)", "NyxSwirl", WhiteText
            },
                () -> {
                    Runnable createBattle1 = () -> {
                        currentScreen = new BattleClass(
                            this, "NYXARION",
                            new UmbraFinalBoss(),
                            true, true, true,
                            "NYXARION_BACKGROUND.png", "NYXARION_TILE.png", "NYXARION_MUSIC.wav",
                            () -> startNextSequence()
                        );
                        setScreen(currentScreen);
                    };
                    createBattle1.run();
                });
            setScreen(currentScreen);
        }


        else if(sequenceCount == 9) {
            currentScreen = new StoryScene(this, new String[]{
                // Nova's dialogue (observant)
                "", "The battle rages, raw energy crackling through the air as Nova and Umbra clash with relentless force. Sparks dance between them, light against shadow, as the ground trembles beneath their power.", "NyxSwirl", WhiteText,
                "", "Nova’s movements grow sharper, her strikes fueled not by anger, but by unyielding resolve. Umbra’s attacks, though fierce, begin to falter—her desperation seeping through the cracks in her defense.", "NyxSwirl", WhiteText,

                // Umbra (strained)
                "Umbra", "How… how can you still stand? I have every advantage…", "NyxSwirl", VioletText,

                // Nova (steady, compassionate)
                "Nova", "Because I never fought for power, Umbra. I fought for hope… for a future where I wouldn’t become you.", "NyxSwirl", NovaTextColor,

                // Narration (Umbra weakening)
                "", "Umbra’s expression wavers, her strength waning as her form flickers like a dying flame.", "NyxSwirl", WhiteText,

                // Umbra (whispering, pleading)
                "Umbra", "I only wanted to fix… what I lost.", "NyxSwirl", VioletText,

                // Nova (softly, finality)
                "Nova", "But you lost yourself trying to chase what’s already gone.", "NyxSwirl", NovaTextColor,

                // Narration (Nova's final act)
                "", "Nova channels the last surge of energy into her device, a radiant wave pulsing outward. The light engulfs Umbra, her shadow dissipating as the echoes of her pain fade into silence.", "NyxSwirl", WhiteText,
                "", "Umbra’s form crumbles, not in violence, but in release—as if, for the first time, she is free from her own torment.", "NyxSwirl", WhiteText,

                // Jina (quiet)
                "Jina", "It’s over… you did it.", "NyxSwirl", OrangeText,

                // Nova (trembling, reflective)
                "Nova", "No, Jina… she did it. In the end… she let go.", "NyxSwirl", NovaTextColor,

                // Narration (peace returns)
                "", "As the darkness fades and light begins to return, the silence is heavy yet peaceful. Nyxarion, once twisted and corrupted, now feels… still.", "NyxSwirl", WhiteText,
                "", "But the weight of what was lost lingers in the air—like a reminder etched into eternity.", "NyxSwirl", WhiteText,

                // Narration (epilogue)
                "", "In her final moments, Umbra didn’t lose… she found the strength to surrender her pain.", "NyxSwirl", WhiteText,
                "", "And in doing so, she gave Nova something even more powerful—clarity. A path not just to save worlds… but to ensure she never loses herself in the process.", "NyxSwirl", WhiteText
            },
                () -> {
                    this.setScreen(new EndCreditsScreen(this));
                });
            setScreen(currentScreen);
        }


    }

    public void displayCharacters() {
        this.setScreen(new DisplayCharacters(this));
    }

    public void showControls() {
        setScreen(new GameControls(this));
    }

    public void restartCurrentGame() {
        if (currentScreen instanceof BattleClass) {
            ((BattleClass) currentScreen).resetCurrentBattle();  // Call the existing method
        } else {
            newGame();  // Fallback for non-battle screens
        }
    }
}
