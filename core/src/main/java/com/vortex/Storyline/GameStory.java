package com.vortex.Storyline;

import java.util.*; //for music

public class GameStory {

    // ANSI Escape Codes (Simplified Names)
    private static final String ITALIC = "\u001B[3m";
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW_BACKGROUND = "\u001B[43m";  // Yellow background for Aetheris
    private static final String GREEN = "\u001B[32m";              // For Xyberia
    private static final String YELLOW = "\u001B[33m";             // For Aetheris
    private static final String BLACK = "\u001B[30m";              // For Planet Void
    private static final String WHITE = "\u001B[37m";              // White for dialogues
    private static final String BLUE = "\u001B[34m";               // Blue for NOVA
    private static final String RED = "\u001B[31m";                // Red text
    private static final String PURPLE = "\u001B[35m";             // Purple for UMBRA
    private static final String BOLD_WHITE = "\u001B[1;37m";       // Bold white text
    private static final String CYAN = "\u001B[36m";               // Cyan text for JINA
    private static final String ArenaRed = "\u001B[1;38;5;196m";
    private static final String NyxarianPurple = "\u001B[1;38;5;135m";

    // Constants
    private static final int TYPING_DELAY = 20; // Delay between words (in milliseconds)
    private final Scanner scanner; // Scanner to read user input

    public GameStory() {
        scanner = new Scanner(System.in); // Initialize the scanner
    }

    /*
     String Builder append guides:

     1. createDescription - for creating description texts
     2. dialogueNova - dialogue text for Nova
     3. createItalicText - text with black highlight and italized, the narrations of the story
     4. dialogueEnemy - dialogue texts for opponents
     5. startFight - commencing fight texts

     private void waitForEnter() = a method that will let the user press enter to go the next dialogue or text, etc..
     private void printWithTypingEffect(String text) throws InterruptedException = prints the string builder methods, and adds a typing effect..
     */

    public void display(int page) throws InterruptedException {

        if (page == 45) {    //UNIVERSE 1 XYBERIA
            // Build the story using StringBuilder for better performance and clarity
            StringBuilder story = new StringBuilder();


            // Add story sections
            story.append(createDescription(
                "Nova, a brilliant quantum physicist and engineer, had always been captivated by the idea of infinite realities. " +
                    "Driven by a desire to push the boundaries of science, she developed a groundbreaking device designed to open gateways " +
                    "between alternate dimensions. But when a critical failure during a high-stakes experiment triggered a catastrophic " +
                    "chain reaction, Nova was violently thrown into the unknown tumbling through a chaotic web of strange and unpredictable universes."
            ));


            story.append(createDescription(
                "Her only way home lies in a broken device, scattered across these universes. " +
                    "As she searches for its fragments, she begins to see that her mission may be about more than just survival-" +
                    "it might reshape reality itself. In this dizzying multiverse, nothing is as it seems, " +
                    "and allies can quickly become enigmas. Nova must navigate" +
                    " carefully, as every decision could alter her fate and that of the multiverse. " +
                    "As she dives deeper into the unknown, she uncovers secrets that could change everything " +
                    "if she can stay ahead of those who seek to control her destiny."
            ));

            story.append(createItalicText(
                "In the dimly lit lab, surrounded by blinking screens and the soft hum of machinery, " +
                    "Nova stood before her latest creation, a sleek, metallic device designed to bridge the gaps between alternate realities. " +
                    "Her heart raced with excitement and trepidation. This was the moment she had spent years preparing for, " +
                    "and now, she was on the brink of discovering the secrets of the multiverse."
            ));

            story.append(dialogueNova("Nova",
                "Alright, Nova, this is it. The moment you've been waiting for. Just a few more adjustments, and we'll unlock the secrets of the multiverse."
            ));

            story.append(createItalicText(
                "She adjusted the settings, her fingers dancing over the controls as she spoke to herself, seeking reassurance."
            ));

            story.append(dialogueNova("Nova",
                "Come on, Nova. You've studied for this. Just focus on the calculations. You've got this!"
            ));

            story.append(createItalicText(
                "But as she cranked up the energy output, a warning light flashed and alarms blared, cutting through her concentration."
            ));

            story.append(dialogueNova("Nova",
                "What? No, no! This can't be happening! Not now!"
            ));

            story.append(createItalicText(
                "Frantically, she scanned the screens with her mind racing."
            ));

            story.append(dialogueNova("Nova",
                "The energy levels are spiking! I need to redirect power-quick!"
            ));

            story.append(createItalicText(
                "As she made adjustments, her voice trembled with fear and determination."
            ));

            story.append(dialogueNova("Nova",
                "Stay calm, stay calm. You've faced worse before. Just a little more fine-tuning!"
            ));

            story.append(createItalicText(
                "Suddenly, the device let out a low hum, growing louder and more chaotic."
            ));


            story.append(dialogueNova("Nova",
                "What are you doing? No, no! Don't overload!"
            ));

            story.append(createItalicText(
                "With a final desperate attempt, she shouted at the machine, as if it could hear her."
            ));

            story.append(dialogueNova("Nova",
                "AAAHHH!  can't let you fail now! Not after everything I've worked for!"
            ));

            story.append(createItalicText(
                "But before she could react, a blinding light enveloped her, and in an instant, she was torn from her lab, and catapulted into the unknown."
            ));

            printWithTypingEffect(story.toString());
        }

        else if (page == 1) {    //UNIVERSE 1 XYBERIA
            // Build the story using StringBuilder for better performance and clarity
            StringBuilder story = new StringBuilder();

            story.append(createDescription(
                "Nova landed hard on a wet street bathed in neon lights. " +
                    "The skyscrapers above twisted into jagged spires, their surfaces covered in flickering holograms and rusted metal. " +
                    "The air was thick with smog and the hum of drones buzzing overhead. " +
                    "Around her, hybrid creatures-half human, half machine-moved through the crowded streets, their faces cold and unreadable."
            ));


            story.append(createItalicText(
                "When the light faded, Nova found herself sprawled on a neon-lit street, " +
                    "the air thick with the scent of burnt circuitry and oil. " +
                    "Towering skyscrapers rose above her, covered with vibrant holograms that flickered erratically."
            ));

            story.append(dialogueNova("Nova",
                "Where am i?"
            ));

            story.append(createItalicText(
                "She glanced at her device, now crackling and broken in her hands, components hanging by mere threads."
            ));


            story.append(dialogueNova("Nova",
                "Ugh! This can't be happening! I spent years perfecting this device but now it's broken! I need to find parts to fix this..."
            ));

            story.append(dialogueNova("Nova",
                "but how?"
            ));

            story.append(createItalicText(
                "As her thoughts raced, Nova looked around, feeling desperate yet determined." +
                    " She clenched her fists, refusing to give up despite the overwhelming odds against her."

            ));

            story.append(dialogueNova("Nova",
                "There has to be something or someone around here, anything, that can help me get back to my lab! I can't give up now, not when I'm so close!"
            ));

            story.append(createItalicText(
                "Suddenly, a group of cyber-enhanced bounty hunters approached, their glowing eyes scanning the area. Nova felt her heart race as they zeroed in on her, sensing her vulnerability."
            ));

            story.append(dialogueEnemy("Bounty Hunter 1",
                "Look what we have here a lost little girl. Hand over your tech, and we might let you go."
            ));

            story.append(createItalicText(
                "Panic surged through her. With no fighting skills, she took a deep breath and tried to think strategically."
            ));

            story.append(dialogueNova("Nova",
                "I'm just a researcher! You don't want to hurt me. I can... I can help you with tech upgrades!"
            ));

            story.append(createItalicText(
                "The hunters hesitated, but one stepped forward, a wicked grin spreading across his face."
            ));

            story.append(dialogueEnemy("Bounty Hunter 2",
                "Oh, we'll take our chances. Lets grab her!"
            ));

            story.append(startFight("Fight Scene Begins - Nova vs. Bounty Hunters"));

            story.append(createItalicText(
                "The first bounty hunter lunged toward her. Nova, relying on her agility, dodged the attack and retaliated with a swift punch, sending the hunter back."
            ));

            story.append(createItalicText(
                "The second hunter charged, but Nova sidestepped and delivered a precise Punch to the Jaw, disarming the enemy with sheer physical force."
            ));

            story.append(dialogueNova("Nova",
                "If you want a fight, you'll get one. (with a hint of nervousness and anxiety)"
            ));

            //BEYOND THIS LINE IS THE NEW ADDED CODE
            printWithTypingEffect(story.toString());
        }

        else if (page == 2) {  //UNIVERSE 1 XYBERIA
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "With a sudden surge of adrenaline, Nova ducked under a series of strikes, using her surroundings to her advantage." +
                    " She jumped off a nearby wall and performed a Dropkick to knock down two more hunters."
            ));

            story.append(createItalicText(
                "As the two bounty hunters fell to the ground, a fierce determination burned within Nova. " +
                    "She could feel her heart racing, the pulsing beat echoing in her ears as she braced for the next wave of attackers. "
            ));

            story.append(createItalicText(
                "Nova kneels beside one of the fallen bounty hunters, inspecting the cybernetic parts on his arm. " +
                    "She carefully detaches a power core and a small circuit board-exactly the components she needs for her device."
            ));

            story.append(dialogueNova("Nova (muttering to herself)",
                "Hmmm,  this should work."
            ));

            story.append(createItalicText(
                "With the parts in hand, she pulled her device from her belt and carefully inserted the power core into her device, " +
                    "followed by the circuit board. The device sputtered, emitting a low hum as energy began to flow through it once again."
            ));

            story.append(dialogueNova("Nova (whispering)",
                "Yes! Just a few more parts and I can get out of here..."
            ));


            story.append(createItalicText(
                "Just then, a commanding voice cut through the chaos."
            ));

            story.append(dialogueEnemy("Cyber boss (overhead, menacing)",
                "Enough!"
            ));


            story.append(createItalicText(
                "From the shadows emerged the boss a mafia like boss, a towering figure clad in dark armor adorned with glowing circuitry. " +
                    "His mechanical enhancements gleamed menacingly in the neon lights, and his eyes glowed a vibrant red, " +
                    "fixating on Nova with a predatory gaze."
            ));

            story.append(dialogueEnemy("Cyber boss",
                "What do you think you're doing? You're out of your league here."
            ));

            story.append(createItalicText(
                "Nova steadied herself, trying to maintain focus."
            ));


            story.append(dialogueNova("Nova(nervous)",
                "...I'll take my chances."
            ));

            story.append(dialogueEnemy("Cyber boss",
                "You're just a little girl playing in a world you can't comprehend. Do you think you can take on my bounty hunters? You'll regret crossing me."
            ));


            story.append(createItalicText(
                "Nova's heart raced as she stared down the towering figure before her. " +
                    "The boss lunged forward, his massive fist swinging toward her with terrifying force. " +
                    "Nova barely dodged, desperation gripped her her usual kicks and punches wouldn't be enough to take him down."
            ));

            story.append(dialogueNova("Nova(nervous)",
                "I... I'm so tired. Why can't i just?"
            ));

            story.append(createItalicText(
                "With her legs wobbling and exhaustion creeping into every muscle, Nova delivers a sharp elbow strike to the boss, but it barely fazes him."
            ));

            story.append(dialogueEnemy("Cyber boss",
                "Pathetic. You can't win with those weak attacks!"
            ));

            story.append(createItalicText(
                "The boss retaliated with a powerful roundhouse kick, and though Nova leaped backward to avoid the blow, the fatigue was setting in fast."
            ));

            story.append(createItalicText(
                "She darted into the shadows, her eyes scanning for a weapon. Spotting a bounty hunter wielding a plasma blade, she seized the moment." +
                    " A swift strike disarmed him, and she snatched the blade just in time to block a massive punch from the boss."
            ));

            story.append(dialogueEnemy("Cyber boss",
                "You think that will save you?"
            ));

            story.append(createItalicText(
                "She struck at his side with a quick jab, but the blade barely left a mark."
            ));

            story.append(dialogueNova("Nova(nervous)",
                "Argh! I'm doomed!"
            ));

            story.append(createItalicText(
                "She darted to the side, narrowly dodging another of his attacks, then ran. Not from fear, but from a growing sense of helplessness."
            ));

            story.append(createItalicText(
                "The Boss charged at her with incredible speed. He swung a metallic arm in an arc, and Nova barely ducked beneath it."
            ));

            story.append(dialogueNova("Nova(nervous)",
                "I can't... I can't fight him i'll be dead in no time!"
            ));

            story.append(createItalicText(
                "With her breath ragged, she backed away, tweaking her damaged device out of desperation." +
                    " Nova ducked into a corner, frantically trying to adjust the power core. The device sputtered, sparks flying. Nova cursed under her breath as the device malfunctioned."
            ));

            story.append(dialogueNova("Nova(nervous)",
                "Work, dang it! I have to get out of here!"
            ));

            story.append(createItalicText(
                "Suddenly, the device gave a loud hum, and a surge of energy shot through her arm. " +
                    "She wasn't sure what she had triggered, but before she could react, the world around her shifted. " +
                    "A blinding light engulfed her, and in an instant, she was pulled from Xyberia and flung into another universe."
            ));

            story.append(dialogueNova("Nova(nervous)",
                "AAAAAAAAHHHHHH!!!"
            ));

            // Print the new story with a typing effect
            printWithTypingEffect(story.toString());
        }



        else if(page == 3){ //UNIVERSE 2 AETHERIS
            StringBuilder story = new StringBuilder();


            story.append(dialogueNova("Nova (breathing heavily and shaken)",
                "What... wh-where am I?"
            ));

            story.append(dialogueNova("Nova",
                "I just hope im not being followed"
            ));

            story.append(createItalicText(
                "As she tried to gather her bearings, she spotted further her location a glowing piece of tech in the wreckage. " +
                    "She noticed a small piece of glowing tech. She picked it up it seemed to pulse with power. " +
                    "It wasn't just any material it was a key component she needed."
            ));

            story.append(dialogueNova("Nova (realizing)",
                "Wait... this could be it. This could give me the firepower I need!"
            ));

            story.append(createItalicText(
                "She slotted the piece into her damaged device. The makeshift gadget hummed to life once more, but this time, it was different. " +
                    "A small display flickered on her wrist. ENERGY BLASTER."
            ));

            story.append(dialogueNova("Nova (surprised, smiling faintly)",
                "Woah... Looks like I've got an upgrade."
            ));

            story.append(createDescription(
                "++Energy Blaster Acquired"
            ));

            story.append(createItalicText(
                "She wipes sweat from her brow, her legs aching from the constant running and fighting from her last battle in Xyberia. " +
                    "Her body screams for rest, but she knows she can't stop now. She clutches the Energy Blaster she recently unlocked, feeling a sliver of reassurance. "
            ));

            story.append(dialogueNova("Nova (gritting her teeth, weary)",
                "Of course... more of you. Couldn't have a break, could I?"
            ));

            story.append(createItalicText(
                "She looks up, eyes widening as a massive Sky Leviathan circles overhead, casting a dark shadow over the island. "
            ));

            story.append(dialogueEnemy("Sky Leviathan (booming from above)",
                "Intruder! You will pay for entering our domain!"
            ));

            story.append(createItalicText(
                "Without warning, a flock of Skyborne Minions swoops down, fast and vicious. Nova straightens, despite her exhaustion."
            ));

            story.append(dialogueNova("Nova (exhaling shakily and gritting her teeth)",
                "Ha! Fine. Let's do this."
            ));

            story.append(createItalicText(
                "Her muscles protest, but she forces herself to move, ready for another battle. There's no time to rest, now her survival depends on it."
            ));

            story.append(startFight("Nova vs. Sky Leviathan Minions"));


            printWithTypingEffect(story.toString());
        }


        else if (page == 4){ //UNIVERSE 2 AETHERIS
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "The remaining minions hesitated. Nova fired again, finishing them off with precision."
            ));

            story.append(createItalicText(
                "With the minions defeated, the Sky Leviathan, ruler of this realm, descends from the storm clouds above, its massive wings casting a shadow over Nova. " +
                    "Its glowing eyes lock onto her as it lands heavily on the island, shaking the very ground."
            ));

            story.append(dialogueEnemy("Sky Leviathan",
                "I will crush you beneath my wings, mortal!"
            ));

            story.append(createItalicText(
                "Nova feels the force of its presence, but she stands her ground, her heart pounding. She knows she has to end this quickly, she's running on fumes."
            ));

            story.append(dialogueNova("Nova (muttering to herself, exhausted)",
                "Just one more fight... I-I can do this."
            ));

            story.append(createItalicText(
                "The Sky Leviathan roars and charges, its massive claws raking the ground. Nova rolls to the side, narrowly avoiding the blow, and counters with an Energy Punch to its side. " +
                    "The impact sends a shockwave through its scales, but the creature barely flinches."
            ));

            story.append(dialogueNova("Nova",
                "That barely scratched it..."
            ));

            story.append(dialogueEnemy("Sky Leviathan",
                "You dare challenge me?"
            ));

            story.append(createItalicText(
                "The Leviathan roared and launched a torrent of wind. Nova expertly dodged, determination burning in her eyes."
            ));

            story.append(startFight("Nova vs. Sky Leviathan"));


            printWithTypingEffect(story.toString());
        }

        else if (page == 5){ //UNIVERSE 2 AETHERIS
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "With a surge of energy, she lunges forward, delivering a series of powerful strikes. " +
                    "After an intense battle, Nova manages to bring down the Leviathan, and the skies briefly clear, signaling her victory."
            ));

            story.append(createItalicText( //doesnt seem tired
                "Breathing heavily, Nova takes a moment to gather herself, the adrenaline still pumping."
            ));

            story.append(dialogueNova("Nova (catching her breath, a triumphant smile breaking through)", //doesnt seem tired like she just fought a sky leviathan and minions
                "I...I did it.. I actually did it!"
            ));

            story.append(createItalicText(
                "As the echoes of battle faded, a sense of calm washed over the island. Nova stood victorious, but the air still crackled with residual tension. " +
                    "Suddenly, a soft glow emerged from the swirling mist."
            ));

            story.append(createItalicText(
                "From the shadows stepped a figure cloaked in a dark, flowing robe that billowed slightly, even without a breeze. " +
                    "The full mask she wore was beautifully crafted, hiding the features beneath. Despite the mask, her eyes shone with a warm light, a blend of mystery and reassurance."
            ));

            story.append(dialogueUmbra("??? (calmly)",
                "Impressive. Not many can defeat a Sky Leviathan on their first try."
            ));

            story.append(createItalicText(
                "Startled, Nova turned to face her, a mix of wariness and curiosity flickering across her face. " +
                    "Despite the stormy skies, the warmth of her smile and the kindness in her gaze instantly eased Nova's tension."
            ));

            story.append(dialogueNova("Nova (frowning, still catching her breath)",
                "...Who are you?"
            ));

            story.append(dialogueUmbra("Umbra (with a knowing smile)",
                "Hmmm... Umbra, Call me Umbra. Let's just say, I have my reasons for being here. And it seems we share a common goal-survival."
            ));

            story.append(createItalicText(
                "Nova hesitates, her instincts telling her to be cautious, but exhaustion clouded her judgment. " +
                    "She watches as Umbra steps closer, her calm demeanor contrasting with the chaos surrounding them."
            ));

            story.append(dialogueNova("Nova (raising an eyebrow)",
                "Survival? I'm just trying to find my way back to my world. How bout you? What are you after?"
            ));

            story.append(createItalicText(
                "Umbra steps closer, her tone calm and reassuring."
            ));

            story.append(dialogueUmbra("Umbra",
                "Perhaps I can help you with that. But you must understand, Aetheris is only the beginning. There are other worlds, and each will test you in ways you can't yet imagine."
            ));

            story.append(createItalicText(
                "Nova's mind raced, but the shard of glowing tech in her hand reminded her of her broken device and the urgent need for repairs."
            ));

            story.append(dialogueNova("Nova",
                "I need to find materials to fix my device. Without it, I'm lost."
            ));

            story.append(dialogueUmbra("Umbra",
                "Then you must seek out the Void-The War-Torn Region Inside Aetheris. It holds remnants of ancient technology, pieces you might find essential."
            ));

            story.append(createItalicText(
                "Nova's eyes widened at the mention of the Void, a place whispered about in fearful tones."
            ));

            story.append(dialogueNova("Nova",
                "What kind of materials?"
            ));

            story.append(dialogueUmbra("Umbra",
                "Elements from the ancient tech-quantum coils and energy conductors. They're scattered throughout the Void, remnants of the battles that shaped this realm."
            ));

            story.append(createItalicText(
                "Nova glances at the shard in her hand, knowing she can't do this alone, but unsure if she can trust Umbra."
            ));

            story.append(dialogueNova("Nova",
                "And you just happen to show up after the fight?"
            ));

            story.append(dialogueUmbra("Umbra (smiling slightly)",
                "Coincidence... or perhaps fate. Either way, you'll need me for the trials ahead."
            ));

            story.append(createItalicText(
                "The storm around them begins to settle, but Nova senses the tension in the air. With no other option in sight, she nods reluctantly."
            ));

            story.append(dialogueNova("Nova (sighing)",
                "Fine. But if you're lying to me…"
            ));

            story.append(dialogueUmbra("Umbra",
                "Why would i?"
            ));

            story.append(createDescription(
                "++Umbra is part of your team now"
            ));

            //Scene: Entering The Void
            story.append(createItalicText(
                "Nova and Umbra stand before the unstable portal, its energy flickering. The air is heavy with tension."
            ));

            story.append(dialogueNova("Nova (glancing at the portal)",
                "Looks like it could tear apart any second."
            ));

            story.append(dialogueUmbra("Umbra (focused)",
                "It will if we don't act fast. Gather the crystals; too much energy, and it'll overload. Too little, and it'll collapse."
            ));

            story.append(createItalicText(
                "Nova gathers glowing crystals, placing them near the portal. The portal stabilizes, pulsing steadily."
            ));

            story.append(dialogueUmbra("Umbra",
                "It's ready. Once inside, stay close. The Void distorts everything."
            ));

            story.append(dialogueNova("Nova (steeling herself)",
                "Let's go."
            ));

            story.append(createItalicText(
                "They step into the swirling portal, the world around them warping into nothingness."
            ));

            //Scene: Inside The Void
            story.append(createItalicText(
                "The Void is cold and disorienting. Faint whispers echo. Shadows flicker at the edges."
            ));

            story.append(dialogueNova("Nova (glancing around)",
                "So, this is The Void..."
            ));

            story.append(dialogueUmbra("Umbra (serious)",
                "Stay alert. The dangers here won't warn you."
            ));

            story.append(createItalicText(
                "They push forward as strange distortions appear fractured images of other realities."
            ));

            story.append(dialogueNova("Nova (under her breath)",
                "What are these…?"
            ));

            story.append(dialogueUmbra("Umbra",
                "Echoes of other worlds. Move fast before they pull us in."
            ));

            //Scene: Journey Through The Void
            story.append(createItalicText(
                "As they tread carefully, dark figures emerge from the mist."
            ));

            story.append(dialogueNova("Nova (gripping her weapon)",
                "We've got company."
            ));

            story.append(dialogueUmbra("Umbra (calm)",
                "Void Sentinels. Focus, we can't let them stop us."
            ));

            printWithTypingEffect(story.toString());
        }

        else if(page == 6){ //UNIVERSE 2 VOID
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "She channels Void energy into Nova's weapon, making it stronger. Nova moves with agility, cutting through the Sentinels with ease."
            ));

            story.append(dialogueNova("Nova (impressed)",
                "Not bad."
            ));

            story.append(dialogueUmbra("Umbra (smiling)",
                "We make a good team."
            ));

            story.append(createItalicText(
                "The last Sentinel dissolves. Nova wipes her brow, catching her breath."
            ));

            story.append(dialogueNova("Nova",
                "That energy thing useful."
            ));

            story.append(dialogueUmbra("Umbra",
                "You fought well. But there's more ahead."
            ));

            story.append(dialogueNova("Nova (determined)",
                "Let's keep going."
            ));

            story.append(createItalicText(
                "They move forward, their partnership solidifying with every step."
            ));

            //Scene: Meeting jina
            story.append(createItalicText(
                "Nova and Umbra tread cautiously through The Void, the shifting shadows warping and distorting the space around them. Out of the swirling mist, a figure appears, cloaked in the same eerie glow as the environment. Jina steps forward, her expression unreadable but her presence commanding."
            ));

            story.append(dialogueJina("??? (calmly)",
                "...Ya'll entering the dangerzone peeps."
            ));

            story.append(dialogueNova("Nova (gripping her weapon tighter, wary but intrigued)",
                "Who are you?"
            ));

            story.append(dialogueJina("Jina (approaching slowly)",
                "Name's Jina, and lemme warn ya, ya'll heading straight into a trap. 'Em bigshot friendo controlin' these area in The Void is pretty... pretty strong... probably stronger than one's you guys have faced."
            ));

            story.append(dialogueUmbra("Umbra (cool, unfazed)",
                "We know the risks."
            ));

            story.append(createItalicText(
                "Jina's gaze shifts briefly to Umbra before locking back onto Nova."
            ));

            story.append(dialogueJina("Jina",
                "Oh Do you now? The Void twists more than just reality. It distorts motives, plays with your brains. Ya'll need more than just yer brawns to survive here."
            ));

            story.append(dialogueNova("Nova (frowning)",
                "Why are you telling us this?"
            ));

            story.append(dialogueJina("Jina (voice steady)",
                "...Because ya at least deserve to know. Be careful as ya move forward."
            ));

            story.append(createItalicText(
                "Nova glances at Umbra, who stays silent, her face unreadable."
            ));

            story.append(dialogueNova("Nova (firmly)",
                "We're in this together. If you want to help, then help. But we don't have time to play games."
            ));

            story.append(dialogueJina("Jina (shrugging slightly)",
                "Just offerin a warning. Take it or leave it."
            ));

            story.append(createItalicText(
                "The tension between the three lingers in the air, but Nova remains focused, her resolve unshaken despite the cryptic message. They press on, Jina falling into step behind them."
            ));

            story.append(createDescription(
                "++Jina is part of your team now"
            ));

            story.append(createItalicText(
                "As they push deeper into The Void, the air grows thicker with energy. Nova feels an odd pull, like something is watching them from just beyond the distortions. Jina stays quiet, observing the environment."
            ));

            story.append(dialogueJina("Jina (quietly, leaning closer to Nova)",
                "Trust yer instincts. In areas like this, appearances can be deceivin."
            ));

            story.append(dialogueNova("Nova (uneasy but determined)",
                "I'm not turning back now."
            ));

            story.append(createItalicText(
                "The atmosphere in The Void becomes even more oppressive as they near their destination. Jina and Umbra walk beside her, silent, but tension hangs in the air."
            ));

            story.append(dialogueNova("Nova (glancing at Jina)",
                "You've fought something like this before?"
            ));

            story.append(dialogueJina("Jina (eyes forward, calm)",
                "I've seen 'nuf to know how deadly 'em are. Keep steady. It ain't fightin til it's sure ya'll are prey."
            ));

            story.append(createItalicText(
                "As they press on, the air grows colder, and a low, rumbling sound vibrates through the space around them. Nova grips her weapon tighter, her heart pounding in sync with the rumbling. The dark shape of a massive structure comes into view ahead. It pulses with energy, like a living thing, and within its core, the faint glow of an enormous creature begins to emerge from the swirling chaos."
            ));

            //Scene: Facing the Boss

            story.append(createItalicText(
                "Nova, Umbra, and Jina stand at the edge of a swirling vortex, a menacing aura radiates from within. The shadows twist and contort, revealing the outline of the boss, a massive creature with dark, shifting forms and glowing eyes."
            ));

            story.append(dialogueEnemy("The Void Lich (with a low, rumbling voice)",
                "Trespassers… You do not belong here."
            ));

            story.append(createItalicText(
                "Nova readies her weapon, her eyes focused on the creature's movements. Umbra steps forward, her hands glowing with dark energy. Jina stays back, observing but prepared."
            ));

            story.append(dialogueNova("Nova (determined)",
                "We've come this far. We're not leaving without what we need."
            ));

            story.append(dialogueEnemy("The Void Lich (snarling)",
                "You will leave with nothing but oblivion."
            ));

            story.append(createItalicText(
                "The boss lets out a deafening roar, launching itself toward them with blinding speed. It strikes with shadowy tendrils, trying to overwhelm the trio."
            ));

            story.append(dialogueJina("Jina (reacting quickly)",
                "Move!"
            ));

            printWithTypingEffect(story.toString());
        }



        else if (page == 7){ //UNIVERSE 2 VOID
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "The boss struggles against their combined might, but its dark energy begins to falter. Seeing their opening, Umbra and Jina coordinate their attacks."
            ));

            story.append(dialogueJina("Jina",
                "On my mark!"
            ));

            story.append(dialogueUmbra("Umbra",
                "Ready… three, two, one-NOW!"
            ));

            story.append(createItalicText(
                "They launch their final coordinated attack. Nova unleashes her Energy Blaster, while Umbra strikes with Shadow Strike, and Jina finishes it off with a powerful Sledge Strike."
            ));

            story.append(dialogueEnemy("Boss (screaming)",
                "No! I will not be defeated by the likes of you!"
            ));

            story.append(createItalicText(
                "The boss dissolves into shadows, leaving behind shimmering shards of energy."
            ));

            story.append(dialogueNova("Nova (catching her breath)",
                "We did it!"
            ));

            story.append(dialogueJina("Jina (smirking)",
                "Eyyyy not bad for our first jam together ya'll."
            ));

            story.append(dialogueUmbra("Umbra (calmly)",
                "This is only the beginning. We need to keep moving. More challenges lie ahead."
            ));

            story.append(createItalicText(
                "After the intense battle with the Void Lich, Nova, Jina, and Umbra regroup, catching their breath as the last remnants of the defeated creature fade into the shadows. The swirling chaos of The Void continues, but a brief calm settles over the area. Nova's thoughts race she's now focused on her mission to repair her device and escape. As they walk, Nova is determined to find the components she needs to fix her device."
            ));

            story.append(dialogueNova("Nova (looking around, thoughtful)",
                "Wait. I gotta find now the materials to fix my device..I think I can find something here."
            ));

            story.append(createItalicText(
                "She moves away from the group and approaches a small patch of The Void where the air shimmers unnaturally. The distortion intensifies, revealing a cluster of crystalline shards embedded in the ground, pulsing faintly with energy."
            ));

            story.append(dialogueNova("Nova (eyes widening)",
                "This… this is one of the materials I need!"
            ));

            story.append(createItalicText(
                "Umbra watches from a distance, her expression unreadable, while Jina steps closer, observing the shards with mild interest."
            ));

            story.append(dialogueJina("Jina",
                "Crystallized Void Energy. Pretty rare, and dangerous. Be careful when ya touch it."
            ));

            story.append(createItalicText(
                "Nova kneels and reaches out carefully, her hand hovering over the crystals. They glow brighter as she touches them, humming with power. She takes a deep breath and extracts a piece, feeling the energy surge through her fingertips."
            ));

            story.append(dialogueNova("Nova (smiling slightly)",
                "This is it! I can finally start fixing my device."
            ));

            story.append(createItalicText(
                "She pulls out her malfunctioning device and immediately begins tweaking it, inserting the shard into a small opening. The device hums to life but suddenly begins to glitch flickering lights, distorted sounds, and small bursts of unstable energy."
            ));

            story.append(dialogueNova("Nova (frustrated, tapping on the device)",
                "Come on… work!"
            ));

            story.append(createItalicText(
                "The device sputters, showing flickering coordinates of multiverses, but it's unstable, jumping between dimensions and settings."
            ));

            story.append(dialogueUmbra("Umbra (stepping closer, calmly)",
                "Let me help. You're forcing it too much."
            ));

            story.append(createItalicText(
                "Nova hesitates but hands Umbra the device. Umbra's hands glow with faint dark energy as she delicately works to stabilize it. Slowly, the glitches subside, though the device still looks incomplete."
            ));

            story.append(dialogueUmbra("Umbra (coolly)",
                "There. It's stable… for now."
            ));

            story.append(dialogueNova("Nova (relieved)",
                "Woah…Thanks, Umbra. But I still need more materials to fully repair it."
            ));

            story.append(createItalicText(
                "Umbra glances at the shimmering shard Nova extracted, then back at the device. She seems to know more than she's letting on."
            ));

            story.append(dialogueUmbra("Umbra (confident)",
                "I know where you can find the rest of what you need. I've… come across this kind of energy before. There's a universe where you'll find exactly what you're looking for."
            ));

            story.append(dialogueNova("Nova (curious)",
                "You know where? How?"
            ));

            story.append(dialogueUmbra("Umbra (with a knowing smile)",
                "Let's just say I've traveled more than you might think. We'll need to head to Trionyx, a realm full of raw energy sources. It's not easy to navigate, but I can get us there."
            ));

            story.append(createItalicText(
                "Nova contemplates for a moment, unsure but eager to continue."
            ));

            story.append(dialogueNova("Nova (decisive)",
                "Alright. Let's set it to take us to Trionyx."
            ));

            story.append(createItalicText(
                "As Nova begins entering the coordinates, Jina, who has been watching them closely, steps forward, her eyes fixed on the device."
            ));

            story.append(dialogueJina("Jina (calmly, but with a warning tone)",
                "Lemme tell ya what friendo, The Void has its tricks... And so does every walker who ventures 'ere. Be careful who ya follow."
            ));

            story.append(dialogueNova("Nova (firmly)",
                "We're in this together. I can handle it."
            ));

            story.append(createItalicText(
                "The device hums as they stabilize it enough to lock on to their next destination. As Nova activates it, the swirling chaos of The Void intensifies, and the coordinates settle on a pulsing light labeled Trionyx."
            ));

            story.append(dialogueUmbra("Umbra (with a slight smile)",
                "Next stop Trionyx."
            ));

            story.append(createItalicText(
                "The device pulses, and in a burst of light, Nova, Umbra, and Jina vanish, leaving behind the twisted remnants of The Void as they hurtle toward their next challenge in the mysterious, energy-filled universe of Trionyx."
            ));

            printWithTypingEffect(story.toString());
        }

        else if(page == 8){
            StringBuilder story = new StringBuilder();


            story.append(createItalicText(
                "Nova, Umbra, and Jina land in the heart of Trionyx, a realm alive with raw, untamed energy. " +
                    "The air crackles with power, and the sky is an ever-shifting mix of vibrant colors. Massive, " +
                    "floating structures loom in the distance, connected by streams of pulsating energy. As they recover " +
                    "from the disorienting shift, Nova instinctively shields her eyes from the brightness."
            ));

            story.append(dialogueNova("Nova (shielding her eyes)",
                "What… is this place?"
            ));

            story.append(dialogueUmbra("Umbra (steadying herself, glancing at the surroundings)",
                "Welcome to Trionyx. The energy here is what you need to repair your device, but extracting it won't be simple. This realm is unpredictable."
            ));

            story.append(createItalicText(
                "As they survey their new surroundings, a sudden pulse of energy streaks across the sky, crashing into " +
                    "a distant structure. The explosion of light catches Nova's attention."
            ));

            story.append(dialogueNova("Nova (focused)",
                "Looks like the energy here isn't just hanging around waiting for us. We'll need to move fast."
            ));

            story.append(dialogueJina("Jina (calmly scanning the terrain)",
                "This place's like live wire. One wrong step, and we're toasted. Ya sure yer device can take this?"
            ));

            story.append(dialogueNova("Nova (determined)",
                "It has to. I can't get home without it."
            ));

            story.append(createItalicText(
                "Umbra remains calm, her eyes locked on the distant floating structures. Her voice cuts through the tension."
            ));

            story.append(dialogueUmbra("Umbra (pointing)",
                "The energy you need is housed in those towers. But they're guarded creatures made of pure energy. They won't let us take it without a fight."
            ));

            story.append(createItalicText(
                "Nova tightens her grip on her weapon, ready for what's ahead."
            ));

            story.append(dialogueNova("Nova (with resolve)",
                "Then let's get to it."
            ));

            story.append(createItalicText(
                "As they approach the next floating structure, a small flying shop suddenly materializes before them, " +
                    "made of glowing, translucent energy. Inside, an alien shopkeeper with three glowing eyes and shimmering " +
                    "skin stands behind a counter. A sign reads Alchemy of the Unseen."
            ));

            story.append(dialogueShopkeeper("Shopkeeper (grinning)",
                "Ah, travelers! You need supplies, I presume? Lucky for you, I have just the things to help you survive Trionyx's madness."
            ));

            story.append(dialogueNova("Nova (raising an eyebrow)",
                "We don't have time for "
            ));

            story.append(dialogueJina("Jina (interrupting)",
                "Hold ya horses, Nova, friendo. Might be a good idea to grab a few supplies, suggestion if you will."
            ));

            story.append(dialogueShopkeeper("Shopkeeper (with enthusiasm)",
                "Smart one! Here's what I've got:"
            ));

            story.append(createItalicText(
                "The screen switches to the shop interface, where the player can now browse and select items to purchase " +
                    "from the shop. The following items are available for purchase, with each displaying its price and description."
            ));

            story.append(createItalicText(
                "PlasmaGel Patch - Minor heal.\n" +
                    "VitalStream Serum - Medium heal.\n" +
                    "HyperHeal Nexus - Great heal.\n" +
                    "MediTech Rejuvenator - Heals all.\n" +
                    "PhaseShift Reviver - Revives a downed ally.\n" +
                    "KinetiBoost Cartridge - Medium attack boost.\n" +
                    "Plasma Overcharger - Great attack boost."
            ));

            story.append(createDescription(
                "Choose items to buy----"
            ));



            printWithTypingEffect(story.toString());

        }

        else if(page == 9){
            StringBuilder story = new StringBuilder();

            story.append(dialogueShopkeeper("Shopkeeper",
                "Excellent choices! You won't regret these. They're the finest energy-infused supplies in this universe."
            ));

            story.append(dialogueNova("Nova (looking at the team)",
                "We've got what we need. Let's hope these supplies are enough for what's coming."
            ));

            //Scene: Encounter with the Energy Sentinels
            story.append(createItalicText(
                "As they approach the nearest floating structure, they spot the first set of Energy Sentinels, glowing fiercely with raw power. The Sentinels hover, their bodies crackling with volatile energy."
            ));

            story.append(dialogueJina("Jina (raising her sledgehammer)",
                "No use sneaking past these groggers."
            ));

            story.append(dialogueUmbra("Umbra (readying herself)",
                "Remember, they're fast. We'll need to hit them hard before they have a chance to adapt."
            ));

            story.append(createItalicText(
                "The Sentinels' Vanguard. A trio of Energy Sentinels appears, circling the group with increasing speed. Their bodies glow with volatile energy, crackling as they prepare to strike."
            ));

            story.append(dialogueNova("Nova (tightening her grip)",
                "Here they come!"
            ));

            story.append(dialogueEnemy("Sentinel (recovering quickly, glowing brighter)",
                "You do not belong here!"
            ));

            story.append(createItalicText(
                "One of the Sentinels charges an energy beam, aiming for Umbra, but she swiftly dodges, her form slipping into the shadows."
            ));

            story.append(dialogueUmbra("Umbra (narrowing her eyes)",
                "You're too slow."
            ));



            printWithTypingEffect(story.toString());
        }

        else if(page == 10){ //UNIVERSE 3 TRIONYX
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "Nova's Energy Punch destabilizes the nearest Sentinel, causing it to explode into particles of light. The remaining two Sentinels, weakened, attempt a final desperate charge. Jina intercepts one with another Sledge Strike, while Umbra uses her shadows to pull the last one into Nova's path."
            ));

            story.append(dialogueNova("Nova (finishing it off with another Energy Punch)",
                "Not anymore."
            ));

            story.append(createItalicText(
                "With the Sentinels defeated, the way to the energy source becomes clear. The pulsing energy tower looms ahead, radiating with power. Nova steps forward, her device glowing in response to the energy around them."
            ));

            story.append(dialogueNova("Nova (taking a deep breath)",
                "This is it. Time to fix what's broken."
            ));

            story.append(createItalicText(
                "As Nova begins to extract the raw energy, Jina keeps watch, her eyes scanning the surroundings. The tension in the air grows as the energy surges into the device, causing it to hum with power once again."
            ));

            story.append(dialogueJina("Jina (quietly)",
                "Hey ya'll... I've got the bad gutsies 'bout this..."
            ));

            story.append(createItalicText(
                "Suddenly, a deep rumble shakes the ground. The energy around them flickers wildly, and a massive form begins to emerge from the heart of the energy tower, a towering Energy Guardian, its body crackling with volatile power, its glowing eyes fixed on Nova."
            ));
            // 2nd boss
            story.append(dialogueEnemy("Bob (voice resonating with power)",
                "This realm's energy is sacred. You shall not wield it for your selfish ends."
            ));

            story.append(dialogueNova("Nova (readying her weapon)",
                "I'm not taking it for myself. But I'll do whatever it takes to finish my mission."
            ));

            story.append(dialogueJina("Jina (gripping her hammer, calm)",
                "We finishin' this fast. No errors."
            ));

            story.append(dialogueUmbra("Umbra (focused)",
                "Strike at its core. That's where it's weakest."
            ));

            story.append(createItalicText(
                "the Energy Guardian unleashes waves of volatile energy, sending bolts flying. The trio spreads out, dodging the chaotic blasts."
            ));

            printWithTypingEffect(story.toString());
        }

        else if(page == 11){
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "Bob The guardian howls in pain, destabilizing as its form shatters into thousands of particles before exploding in a burst of raw energy. The ground shakes violently as it lets out a deafening roar, scattering light before vanishing."
            ));

            story.append(dialogueEnemy("Bob (fading, voice echoing)",
                "You… have not won… this realm… will consume you..."
            ));

            story.append(createItalicText(
                "The Guardian disappears, leaving behind a swirling vortex of energy where its core once was. Nova, panting, watches as the energy slowly dissipates into the air."
            ));

            //Post-Battle: The Aftermath

            story.append(createItalicText(
                "Amidst the dissipating energy of the fallen Energy Guardian, a mesmerizing vortex swirls around Nova. She stands rooted, feeling an overwhelming surge of power course through her."
            ));

            story.append(createItalicText(
                "The energy spirals towards her, and she clenches her fists, eyes wide in astonishment."
            ));

            story.append(dialogueNova("Nova",
                "What... What's happening to me?"
            ));

            story.append(createItalicText(
                "The energy converges, wrapping her in a radiant glow. Her device pulses in harmony with the energy, awakening something deep within."
            ));

            story.append(createItalicText(
                "Jina steps back, eyes wide in awe."
            ));

            story.append(dialogueJina("Jina",
                "Wha... Yer merging with the thing!"
            ));

            story.append(createItalicText(
                "Umbra watches intently, her gaze sharp."
            ));

            story.append(dialogueUmbra("Umbra",
                "You're absorbing the Guardian's power."
            ));

            story.append(createItalicText(
                "A brilliant burst of light erupts from Nova, illuminating the battlefield. As the radiance fades, her form pulses with multidimensional energy, enhanced and vibrant."
            ));

            story.append(createItalicText(
                "Nova breathes deeply, feeling the raw power flow through her veins, her hands glowing with a kaleidoscope of colors each pulse reflecting a universe she's traversed."
            ));

            story.append(dialogueNova("Nova",
                "I can feel it... a new power connecting me to every universe."
            ));

            story.append(createItalicText(
                "She raises her hands, releasing waves of energy that ripple outward, harnessing the multiverse's essence."
            ));

            story.append(dialogueNova("Nova",
                "I just unlocked my ultimate skill... Multidimensional Blast!"
            ));

            story.append(createDescription(
                "++Ultimate Acquired: Multidimensional Blast"
            ));

            story.append(createItalicText(
                "Jina grins, her pride evident."
            ));

            story.append(dialogueJina("Jina",
                "Yer quite a force to be reckoned with now!"
            ));

            story.append(createItalicText(
                "Umbra smirks, intrigue sparking in her eyes."
            ));

            story.append(createItalicText(
                "Umbra nods, a hint of a smile on her lips."
            ));

            story.append(dialogueUmbra("Umbra",
                "But remember, such power comes with great responsibility."
            ));

            story.append(createItalicText(
                "As the dust settles, the adrenaline fades, and Nova sinks to her knees, panting. She glances at her device, still humming with energy but not fully functional."
            ));

            story.append(dialogueNova("Nova",
                "It's charged… but still not fixed."
            ));

            story.append(createItalicText(
                "The smile vanishes as reality sets in."
            ));

            story.append(createItalicText(
                "Jina's expression shifts to concern, scanning the horizon."
            ));

            story.append(dialogueJina("Jina",
                "We can't let our alarms down, there could be more groggers waitin."
            ));

            story.append(createItalicText(
                "Umbra approaches, studying the device intently."
            ));

            story.append(dialogueUmbra("Umbra",
                "It holds enough power for now, but you need something more to stabilize it."
            ));

            story.append(createItalicText(
                "Nova's frustration boils over as she clenches her fists, her voice steady but filled with determination."
            ));

            story.append(dialogueNova("Nova",
                "I know there's more out there something crucial that I need to fix this. But where do I even start?"
            ));

            story.append(createItalicText(
                "Umbra's eyes gleam with understanding, a plan forming in her mind."
            ));

            story.append(dialogueUmbra("Umbra",
                "There's a place I know an ancient site that holds untapped energy. It might just have what you need."
            ));

            story.append(createItalicText(
                "Nova looks up, curiosity ignited."
            ));

            story.append(dialogueNova("Nova",
                "Really? Where is it?"
            ));

            story.append(createItalicText(
                "Umbra nods, her expression serious."
            ));

            story.append(dialogueUmbra("Umbra",
                "I can input the coordinates into your device. We need to act fast."
            ));

            story.append(createItalicText(
                "She quickly enters the coordinates, her fingers deftly navigating the controls. The device hums to life, a hopeful glow surrounding it."
            ));

            story.append(dialogueJina("Jina",
                "Well...Let's hope this sends us to what we're lookin' for."
            ));

            story.append(createItalicText(
                "Nova stands tall, a fierce determination burning in her eyes."
            ));

            story.append(dialogueNova("Nova",
                "I refuse to be powerless. Let's move out!"
            ));

            story.append(createItalicText(
                "They share a moment of silent understanding, an unbreakable bond forming as they prepare to face whatever challenges lie ahead. Together, they step into the unknown, ready to reclaim their power."
            ));

            printWithTypingEffect(story.toString());
        }

        else if(page == 12){
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "The trio steps into Universe 4, known as Vortexia a chaotic battleground filled with swirling energy and a landscape that shifts like a tempest. Massive, ancient structures loom overhead, while the ground pulses with the anticipation of battles yet to come."
            ));

            story.append(dialogueNova("Nova (gripping her device, eyes sparkling with determination)",
                "This place hums with energy... It feels like it's alive. We must be close to what I need."
            ));

            story.append(dialogueJina("Jina (alert, scanning the horizon)",
                "Hold steady. Vortexia here's notorious for em fierce lookin guardians."
            ));

            story.append(dialogueUmbra("Umbra (with a sly smile)",
                "And hidden treasures. Let's seize what we came for before the arena claims us."
            ));

            //Scene 2 First encounter

            story.append(createItalicText(
                "The ground trembles as a group of Arena Guardians, armored warriors crackling with energy, emerge from the shadows, weapons gleaming. " +
                    "They encircle the trio, growling menacingly."
            ));

            story.append(dialogueNova("Nova (voice steady, fists clenched)",
                "Looks like the welcome party's here."
            ));

            printWithTypingEffect(story.toString());
        }


        else if(page == 13){
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "With the Guardians defeated, the trio navigates through the arena, encountering traps and energy fields that spark ominously. " +
                    "They approach a massive chasm filled with swirling energy, leading to a hidden chamber."
            ));

            story.append(dialogueJina("Jina (eyes gleaming)",
                "That chamber over ere looks promising. It could probanly hold the key to yer thingy device."
            ));

            story.append(dialogueNova("Nova (nodding, excitement building)",
                "Let's check it out!"
            ));

            story.append(createItalicText(
                "As they near the chamber, a colossal Energy Wyrm a serpentine creature of pure energy emerges, blocking their path with a thunderous roar."
            ));

            story.append(createItalicText(
                "Scene 4: Confronting the Energy Wyrm"
            ));

            //Confronting the energy wyrm
            story.append(createItalicText(
                "The Wyrm lets out a deafening roar, sending shockwaves through the ground. The trio readies themselves, tension palpable."
            ));

            story.append(dialogueNova("Nova (voice firm)",
                "Stay focused! We take it down together. Jina, hit its flank! Umbra, disorient it!"
            ));

            printWithTypingEffect(story.toString());
        }

        else if(page == 14){
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "With coordinated attacks, they land powerful blows until the Wyrm finally collapses, exploding in a burst of energy. " +
                    "Amid the fading light, a shimmering crystal floats down the material Nova needs."
            ));

            //Scene 5 Securing the Crystal
            story.append(createItalicText(
                "Nova kneels to pick up the crystal, her eyes wide with exhilaration."
            ));

            story.append(dialogueNova("Nova",
                "Yes! With this, I can finally fix my device!"
            ));

            story.append(createItalicText(
                "She sets her device down on a nearby flat surface, focusing intently."
            ));

            //Scene 5.2: Battle with the Obsidian Golems (Summoned by Umbra the not goat)/////////////////////////////////////////////////

            story.append(createItalicText(
                "Just as Nova secures the crystal, the ground rumbles violently. Two towering Obsidian Golems hulking creatures of solid black stone " +
                    "with glowing cracks of energy-emerge, their red eyes burning with malice. They lumber towards the trio, their massive fists leaving " +
                    "deep craters in the ground as they move."
            ));

            story.append(dialogueNova("Nova (sharply, raising her device)",
                "More trouble! Stay on your guard!"
            ));

            story.append(dialogueJina("Jina (gripping her sledgehammer, frowning)",
                "These guys don't look friendly. We smash em before they smash us!"
            ));

            printWithTypingEffect(story.toString());

        }

        else if(page == 15) {
            StringBuilder story = new StringBuilder();

            story.append(dialogueJina("Jina (panting, wiping sweat from her brow)",
                "These fights keep getting tougher."
            ));

            story.append(dialogueNova("Nova (narrowing her eyes, suspiciously glancing at Umbra)",
                "Yeah… almost as if they were sent after us."
            ));

            //Scene 5.3: Ambush by the Phantom Assassins (Summoned nasad ni Umbra)

            story.append(createItalicText(
                "Before Nova can even catch her breath, shadows flicker in the corners of the arena. Phantom Assassins, spectral figures with swift, " +
                    "deadly blades, emerge from the darkness, moving with lightning speed."
            ));

            story.append(dialogueNova("Nova (alarmed, tightening her grip on her device)",
                "Not again... Where are these things coming from?"
            ));

            printWithTypingEffect(story.toString());

        }

        else if(page == 16) {
            StringBuilder story = new StringBuilder();

            //Scene 6: Fixing the Device

            story.append(dialogueNova("Nova (determined)",
                "Alright, let's get this done."
            ));

            story.append(createItalicText(
                "Nova kneels, finally setting her device on a flat surface. She begins the meticulous work of fixing it, carefully placing the crystal into position. " +
                    "As the energy flows through the device, it flickers and hums with life. Sparks fly, but Nova keeps her focus steady, her hands moving swiftly."
            ));

            story.append(dialogueJina("Jina (watching closely)",
                "It's... workin? Yer really doing it!"
            ));

            story.append(createItalicText(
                "Nova narrows her eyes, sweat beading on her forehead. The device whirs and pulses, emitting a bright glow as it begins to stabilize."
            ));

            story.append(dialogueNova("Nova (under her breath, focusing)",
                "Come on… just a little more…"
            ));

            story.append(createItalicText(
                "With a final twist, the device locks into place. A pulse of energy surges through it, and the glow fades to a steady, controlled light. Nova steps back, holding her breath as she watches it."
            ));

            story.append(dialogueNova("Nova (relieved, a triumphant smile forming)",
                "It's fixed. I did it."
            ));

            story.append(createItalicText(
                "She picks up the device, feeling its power surge steadily beneath her fingertips. For a moment, Nova allows herself to feel the weight of her accomplishment, a sense of fulfillment washing over her."
            ));

            story.append(createItalicText(
                "Umbra steps forward, a glint of mischief in her eyes."
            ));

            story.append(dialogueUmbra("Umbra",
                "That's a start, but what if I told you there's something even more powerful? Something that could elevate your device beyond your wildest dreams?"
            ));

            story.append(dialogueJina("Jina (cautiously, stepping closer)",
                "Nova, I think it would be better if we stick to the plan..."
            ));

            story.append(dialogueNova("Nova (frowning, glancing between them)",
                "What do you mean? What's in the next universe?"
            ));

            story.append(createItalicText(
                "Umbra leans in, her voice low and enticing."
            ));

            story.append(dialogueUmbra("Umbra",
                "This universe holds resources that could amplify your abilities to an unparalleled level. Just imagine what you could achieve!"
            ));

            story.append(dialogueJina("Jina (warningly)",
                "But we just fought our way here! We can't just rush to a place in who knows where without a plan."
            ));

            story.append(createItalicText(
                "Nova's heart races, caught between their contrasting perspectives. She grips the crystal tightly."
            ));

            story.append(dialogueNova("Nova (conflicted, sighing)",
                "I need this upgrade. But…"
            ));

            story.append(createItalicText(
                "Her resolve strengthens, and desire ignites within her."
            ));

            story.append(dialogueNova("Nova",
                "Alright, let's see what that universe has to offer."
            ));

            //Transitioning to the Fifth Universe

            story.append(createItalicText(
                "With the crystal in hand, Nova activates her device, inputting the coordinates for the Fifth Universe. The air crackles around them as they prepare to leave Vortexia behind."
            ));

            story.append(dialogueUmbra("Umbra (smirking, anticipation in her voice)",
                "This adventure is just beginning, Nova. You won't forget it."
            ));

            story.append(createItalicText(
                "As they vanish into a flash of light, Jina looks back, unease etched on her face, uncertain of the trials yet to come."
            ));

            printWithTypingEffect(story.toString());

        }

        else if(page == 17){
            StringBuilder story = new StringBuilder();


            //Setting: Arrival in Universe 5
            story.append(createItalicText(
                "A flash of bright light fades, and Nova, Jina, and Umbra find themselves in a nightmarish world. The sky is a sickly gray, with massive rifts tearing through it, flickering with unstable energy. Shadows creep along the ground as jagged, rusted machines lie abandoned. Debris scatters with every chilling gust of wind, and a faint hum pulses through the air the remnants of energy that still haunt this barren land."
            ));


            //Scene 1: Entering the Ruins
            story.append(createItalicText(
                "Nova stands in stunned silence, her eyes sweeping across the shattered landscape. Beside her, Jina remains tense, scanning for signs of movement. Umbra observes them, a faint, unreadable smile on her face."
            ));


            story.append(dialogueNova("Nova (confused, whispering)",
                "What… happened here?"
            ));


            story.append(dialogueJina("Jina (tense, scanning their surroundings)",
                "This place... nothin but ruins n rocks. Are ya sure there's anythin left that could help us, Umbra..??"
            ));


            story.append(createItalicText(
                "Umbra steps forward, her voice is calm yet unsettling."
            ));


            story.append(dialogueUmbra("Umbra (haunted)",
                "What you need is here, Nova. The energy in this world may be broken, but that doesn't make it useless. It's far more… potent."
            ));


            story.append(dialogueNova("Nova (studying Umbra closely, her suspicion growing)",
                "You seem familiar with this world, Umbra. Just what exactly happened here?"
            ));


            story.append(dialogueUmbra("Umbra (gaze distant, voice barely a whisper)",
                "What happened here was ambition. To unlock the secrets of multiversal power… but some ambitions require sacrifice."
            ));


            story.append(createItalicText(
                "Jina glares at Umbra, distrust shadowing her face."
            ));


            story.append(dialogueJina("Jina (accusingly, eyes narrowed)",
                "Ya know... ya keep talking like ya know this world. Are ya tellin us the truth, Umbra? How do we know this isn't some sort of trap or somethin?"
            ));


            story.append(dialogueUmbra("Umbra (smiling slightly, voice tinged with irony)",
                "A trap? Perhaps… but it's more of a gift if you know how to claim it."
            ));


            story.append(dialogueNova("Nova (determined, yet wary)",
                "I'll take my chances. Lead us to what we need."
            ));


            //Scene 2: Encounter with the Minions - A Glimpse of the Past
            story.append(createItalicText(
                "Umbra guides them through the ruins, her footsteps echoing softly. The silence is suffocating until a distant clinking sound emerges, growing louder. From behind the crumbling walls, shadowy figures emerge distorted, hunched creatures with remnants of circuitry embedded in their flesh. They appear both organic and machine, their eyes glowing with a dull, sinister light."
            ));


            story.append(dialogueNova("Nova (whispering to herself, unsettled)",
                "Wha.. What are those… things?"
            ));


            story.append(dialogueUmbra("Umbra (calm, almost detached)",
                "Remnants of my... Remnants of the experiments done here. They were once prototypes, vessels for gathering energy."
            ));


            story.append(createItalicText(
                "The minions move with jerky, unnatural steps, encircling the group."
            ));


            story.append(dialogueJina("Jina (bracing herself, voice low)",
                "Well, whatever they are right now, they're for sure ain't no friendlies."
            ));


            printWithTypingEffect(story.toString());


        }


        else if(page == 18) {
            StringBuilder story = new StringBuilder();


            //after FIGHT SCENE: The Obsidian Drones


            story.append(dialogueJina("Jina (panting, surveying the wreckage)",
                "That was way too close for comfort ya'll."
            ));


            story.append(dialogueUmbra("Umbra (her eyes narrowing as she assesses the destruction)",
                "Not close enough… they were just the beginning. We must push forward; the real challenge lies ahead."
            ));


            //Scene 3: Encounter with The Ancient Guardian's Awakening(Boss)
            story.append(createItalicText(
                "As Nova catches her breath, the ground suddenly quakes violently, and from the shadows emerges a towering figure the Eclipse Warden. Clad in dark, gleaming armor etched with pulsating runes, its blazing eyes lock onto the trio with an intensity that sends a chill down their spines. Raising a colossal blade of pure energy, it speaks, its voice reverberating across the landscape."
            ));


            story.append(dialogueEnemy("Eclipse Warden",
                "Trespassers in Nyxarion. Only those deemed worthy may pass. Prepare yourselves for judgment."
            ));


            printWithTypingEffect(story.toString());

        }


        else if(page == 19) {
            StringBuilder story = new StringBuilder();


            story.append(createItalicText(
                "The Warden collapses, leaving behind a pulsing, dark crystal."
            ));


            story.append(dialogueNova("Nova (gasping, racing toward the fallen Warden, eyes wide with anticipation)",
                "This… this is it! The power I need to enhance my device!"
            ));


            story.append(createItalicText(
                "She reaches for the pulsating dark crystal, energy thrumming beneath her fingertips, igniting a connection within her. A brilliant light fills the room, illuminating their path as they brace for the dangers ahead."
            ));


            //Scene 4: A Twisted Revelation
            story.append(createItalicText(
                "The air around Nova crackles with energy as she stares at the pulsating dark crystal, its surface swirling with shadows and light."
            ));


            story.append(dialogueNova("Nova (eyes shimmering with determination, her voice steady)",
                "This crystal… it feels alive. I can sense its power, its potential to amplify my device."
            ));


            story.append(createItalicText(
                "As Nova carefully places the crystal into her device, a surge of energy courses through her, the fusion illuminating the darkened room with brilliant hues. The device hums to life, radiating an aura of newfound strength."
            ));


            story.append(dialogueNova("Nova (grinning, filled with exhilaration)",
                "It worked! This power will change everything!"
            ));


            story.append(dialogueUmbra("Umbra (stepping forward, her tone dripping with intent)",
                "Congratulations, Nova. But did you really think I'd let you walk out of here with it?"
            ));


            story.append(createItalicText(
                "Nova's expression shifts from joy to disbelief as Umbra's words sink in. The shadows around Umbra deepen, swirling like a tempest, revealing her true intentions."
            ));


            story.append(dialogueNova("Nova (confused)",
                "Wha.. What do you mean, Umbra? I.. I don't understand you."
            ));


            story.append(dialogueUmbra("Umbra (smirking, shadows flickering around her)",
                "You see, the power you now possess is exactly what I've been searching for. It's time for you to share it, willingly or not."
            ));


            story.append(dialogueUmbra("Umbra (distant, voice tinged with sorrow)",
                "This place was built by someone like you, Nova someone who defied limits, seeking multiversal power… only to be left in the ruins of shattered dreams and unrelenting ambition."
            ));


            story.append(createItalicText(
                "Nova takes a step back, the realization dawning on her, but she struggles to accept it. Her eyes widen, searching for answers in the fragments around them."
            ));


            story.append(dialogueNova("Nova (voice quivering, horror seeping in)",
                "This world… Umbra, you knew it because… you're from here, aren't you?"
            ));


            story.append(createItalicText(
                "Umbra pauses, her shoulders slumping as though the weight of her past is finally too much to bear. She reaches up and, with a slow, deliberate movement, removes her mask. Beneath it is a face that mirrors Nova's but is etched with the scars of battle and despair, eyes darkened by loss and regret. She holds Nova's gaze, allowing her to see the truth."
            ));


            story.append(dialogueUmbra("Umbra (quietly, resigned, her voice a mixture of pain and anger)",
                "Yes, Nova. I am… or rather, I was you. I am the version of you who sacrificed everything, who gambled with life and worlds to try and create something greater. I failed, Nova. Every experiment, every risk… all of it fell to ruin. This " +
                    "is the price of ambition unbridled."
            ));


            story.append(createItalicText(
                "Nova stares at Umbra, the weight of her words settling into her heart. She feels a mixture of pity and fear, recognizing the twisted reflection of her own desires in Umbra's anguish."
            ));


            story.append(dialogueNova("Nova (whispering, horror etched on her face)",
                "You… you brought us here to take everything I've gained, didn't you?"
            ));


            story.append(createItalicText(
                "Umbra's lips curl into a bitter smile, her expression one of both pride and resignation."
            ));


            story.append(dialogueUmbra("Umbra (her voice dark, with a twisted sense of triumph)",
                "You don't understand, Nova. I didn't just bring you here-I created this chance, this world, because I need what you have. Your victories, your strength, every skill you've fought to gain… I need it all to open the door to greater dark energies, to fix my past failures."
            ));


            story.append(createItalicText(
                "(Nova, caught in the tide of betrayal, feels the weight of Umbra's pain as well as the danger it poses.)"
            ));


            story.append(dialogueNova("Nova (hurt and resolute)",
                "I trusted you, Umbra. You're not taking anything from me!"
            ));


            story.append(dialogueUmbra("Umbra (smirking, her voice laced with malice)",
                "This is the end for you, Nova. Once the Abyssal Dominion is opened, I will have all the power I need to correct my failures, to surpass every limit!"
            ));


            story.append(dialogueNova("Nova (glaring, her fists crackling with energy)",
                "You won't get away with this, Umbra. I'll stop you even if I have to tear that portal apart myself!"
            ));


            printWithTypingEffect(story.toString());

        }

        //Nova wins
        else if(page == 20){
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "Umbra's shadowy aura fractures, the remnants of her dark energy spiraling into nothingness. With a guttural cry, Umbra collapses to the ground, her form flickering and fading as the Abyssal Dominion begins to close. The oppressive energy dissipates, and a soft, golden light filters into the ruins, painting the aftermath with a sense of quiet peace."
            ));

            story.append(dialogueJina("Jina (breathing heavily, leaning on her weapon for support)",
                "We did it… Nova, we really did it."
            ));

            story.append(dialogueNova("Nova (panting, wiping sweat from her brow, her voice trembling with exhaustion and relief)",
                "It's over, Jina. Umbra's gone. We… won."
            ));

            story.append(createItalicText(
                "Nova collapses to her knees, the weight of the battle finally catching up to her. Jina limps over, kneeling beside her, both of them staring at the now-quiet battlefield where Umbra made her final stand. The silence feels heavy, but not oppressive like the end of a long, harrowing journey."
            ));

            story.append(dialogueJina("Jina (smiling faintly, her voice warm and full of pride)",
                "All those fights, Nova all those battles we pushed through. This was the hardest one, but look at us. We're still standing."
            ));

            story.append(dialogueNova("Nova (nodding slowly, her gaze distant as she catches her breath)",
                "It wasn't just the hardest, Jina. It was… everything. Every fight, every loss, every moment we thought we wouldn't make it. It all brought us here to this moment."
            ));

            story.append(createItalicText(
                "Jina lets out a soft laugh, brushing away the dirt on her face as her expression softens. She watches Nova pick up her device, now glowing softly. The crystal inside, once a chaotic storm of energy, radiates with a calm, steady brilliance."
            ));

            story.append(dialogueNova("Nova (gently running her fingers over the device, her voice resolute)",
                "This isn't just our victory, Jina. It's for every world we saved. Every time we kept going, even when it felt impossible. We've proven that no matter how dark things get, there's always a way forward."
            ));

            story.append(createItalicText(
                "Jina nods, her eyes reflecting both pride and understanding. She rests a hand on Nova's shoulder, her voice filled with conviction."
            ));

            story.append(dialogueJina("Jina",
                "And we found that way together, Nova. Because this? This isn't just the end of our fight. It's the start of something bigger something worth fighting for."
            ));

            story.append(createItalicText(
                "Nova looks at Jina, her tired smile warming. She extends her hand, pulling Jina to her feet. Together, they stand amidst the ruins, their silhouettes framed by the golden light streaming through the cracks in the walls."
            ));

            story.append(dialogueJina("Jina (grinning, her voice light but filled with meaning)",
                "Not bad for a duo who started out fumbling through our first fight, huh?"
            ));

            story.append(dialogueNova("Nova (laughing softly)",
                "Not bad at all. From two lost fighters to… well, multiverse saviors, I guess. Sounds a little grand, doesn't it?"
            ));

            story.append(dialogueJina("Jina (smirking)",
                "Grand, sure. But I'd say we've earned it."
            ));

            story.append(createItalicText(
                "The two turn toward the glowing portal ahead, the path to their next adventure. Nova hesitates for a moment, glancing back at the battlefield. Her expression shifts a mix of pride, nostalgia, and the weight of everything they've endured."
            ));

            story.append(dialogueNova("Nova (whispering, her voice steady and full of hope)",
                "We've come so far, Jina. Through every world, every challenge. And there's so much more to do."
            ));

            story.append(createItalicText(
                "Jina steps beside her, giving her a confident nod."
            ));

            story.append(dialogueJina("Jina",
                "Then let's keep going. Together."
            ));

            story.append(createItalicText(
                "They step into the light of the multiverse portal, their silhouettes merging as they disappear into the glow. The portal closes behind them, leaving the ruins silent but no longer lifeless. The golden light remains, a testament to their triumph a reminder of their resilience, their bond, and the hope they've restored to countless worlds."
            ));

            story.append(createItalicText(
                "In the face of overwhelming darkness, they stood firm. Through every loss and every battle, they carried the light of hope. And now, as their journey continues, that light burns brighter than ever a beacon for all who believe in the power of perseverance, unity, and the courage to keep moving forward."
            ));



            printWithTypingEffect(story.toString());
        }

        //Umbra wins
        else if(page == 21){
            StringBuilder story = new StringBuilder();

            story.append(createItalicText(
                "Umbra stands over Nova, a wicked smile spreading across her face. In her grasp, she holds Nova's device, pulsating with energy."
            ));

            story.append(dialogueUmbra("Umbra (triumphantly)",
                "At last, the power of the multiverse is within my grasp! With your device, I will reshape Nyxarion and the whole multiverse into my dominion!"
            ));

            story.append(createItalicText(
                "She activates the device, and dark energy flows around her, illuminating the ruins in a sinister glow. The landscape of Nyxarion begins to transform, twisted spires rising from the ground as Umbra commands her minions to rise."
            ));

            story.append(dialogueUmbra("Umbra (loudly, her voice echoing)",
                "Bow before me! This world is mine to command! We shall reclaim what was lost!"
            ));

            story.append(createItalicText(
                "The shadows writhe and take form, becoming Umbra's loyal minions, ready to serve her once more. The atmosphere thickens with her dark ambition, suffocating the last remnants of hope."
            ));

            // Epilogue: A Life of Despair
            story.append(createItalicText(
                "Nova, trapped in the depths of her mind, feels the weight of hopelessness as she watches Umbra ascend to power, her device illuminating the darkened sky."
            ));

            story.append(dialogueNova("Nova (voice trembling, tears welling)",
                "This can't be how it ends… I fought so hard…"
            ));

            story.append(createItalicText(
                "But Umbra, consumed by her newfound strength, pays no heed to Nova's despair. She revels in her triumph, her voice booming through the broken world."
            ));

            story.append(dialogueUmbra("Umbra (laughing menacingly)",
                "You thought you could defy me? Your hope is a pathetic illusion! Welcome to your new reality, Nova. Darkness reigns, and your dreams are nothing but ashes!"
            ));

            story.append(createItalicText(
                "Umbra's laughter echoes through the ruins of Nyxarion, resonating with malevolence as the shadows deepen around Nova. She realizes she is trapped in this dark realm, unable to return to her universe, her spirit crushed under the weight of Umbra's power."
            ));

            story.append(dialogueNova("Nova (whispering, defeated)",
                "I'm... a failure, I lost...."
            ));

            story.append(createItalicText(
                "The screen fades to black, leaving only the chilling echoes of Umbra's laughter as she stands atop the ruins, a dark queen in a realm consumed by despair, while Nova is left to grapple with her hopeless fate."
            ));


            printWithTypingEffect(story.toString());
        }

        else if(page == 22){
            StringBuilder story = new StringBuilder();

            story.append(createCredits(
                "\n\n\t\t\t\t\t\t\t\t\t\t\t--------------------------------------------\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t                     VORTEX\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t        A Heartfelt Journey of Creation\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t--------------------------------------------\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t             Leader and Visionary\n\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t                 Gino Sarsonas\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t   (Our fearless leader who inspired us to\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t           push boundaries and dream big)\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t                  DEVELOPERS\n\n\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t                Code Masters\n\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t                Daniel Garcia\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t            Sophia Bianca Aloria\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t   (Turning lines of code into a seamless\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t     world of innovation and interaction)\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t                Story Weavers\n\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t               Ashley Igonia\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t            Michelle Marie Habon\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t   (Infusing our creation with meaning and\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t            heart, giving it a soul)\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t--------------------------------------------\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t                 Special Thanks\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t--------------------------------------------\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t   To our mentors and supporters, who fueled\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t   our dreams with wisdom and encouragement,\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t        thank you for believing in us.\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t    Our deepest gratitude to our dear Sir Khai,\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t   Without his guidance and passion for teaching\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t us Object-Oriented Programming (OOP), this project\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t    would not have been possible. The skills and\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t    knowledge he imparted allowed us to create\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t something truly special. Thank you, Sir Khai, for\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t     inspiring us to bring our ideas to life.\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t   And to everyone who experiences this project:\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t   Thank you for letting us share our story with\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t    you. Your time and interest mean the world\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t                    to us.\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t--------------------------------------------\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t                  Our Message\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t--------------------------------------------\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t   This project is more than a culmination of\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t    efforts—it's a reflection of our growth,\n" +
                    "\t\t\t\t\t\t\t\t\t\t\tour camaraderie, and our dreams. Every keystroke,\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t   every idea, every challenge overcame: it all\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t         led us here, to this moment.\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t   May it inspire you to believe in your journey,\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t    to pursue your passions fearlessly, and to\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t    remember that behind every great creation is a\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t    team driven by love, laughter, and the desire\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t          to make something beautiful.\n\n"
            ));

            story.append(createCredits(
                "\t\t\t\t\t\t\t\t\t\t\t--------------------------------------------\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t           The End... or perhaps\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t     the Beginning of Something New\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t--------------------------------------------\n"
            ));




            printWithTypingEffect(story.toString());
        }
    }




    // Helper method to format descriptions (in white)
    private String createDescription(String text) {
        return BOLD_WHITE + text + RESET + "\n\n";
    }

    // Helper method to format italicized text (in white)
    private String createItalicText(String text) {
        return ITALIC + WHITE + text + RESET + "\n\n";
    }

    // Helper method to format Nova's dialogue (in blue)
    private String dialogueNova(String speaker, String text) {
        return BLUE + speaker + ": \"" + text + "\"" + RESET + "\n\n";
    }

    private String dialogueUmbra(String speaker, String text) {
        return PURPLE + speaker + ": \"" + text + "\"" + RESET + "\n\n";
    }

    private String dialogueJina(String speaker, String text) {
        return YELLOW + speaker + ": \"" + text + "\"" + RESET + "\n\n";
    }

    private String dialogueShopkeeper(String speaker, String text) {
        return CYAN + speaker + ": \"" + text + "\"" + RESET + "\n\n";
    }

    // Helper method to format enemy's dialogue (in red)
    private String dialogueEnemy(String speaker, String text) {
        return RED + speaker + ": \"" + text + "\"" + RESET + "\n\n";
    }

    private String Xyberia(String text) {
        return  GREEN + text + RESET + "\n\n";
    }

    private String Aetheris(String text) {
        return YELLOW + text + RESET + "\n\n";
    }

    private String PlanetVoid(String text) {
        return BLACK + text + RESET + "\n\n";
    }

    private String Vortexia(String text) {
        return ArenaRed + text + RESET + "\n\n";
    }

    private String Nyxarion(String text) {
        return NyxarianPurple + text + RESET + "\n\n";
    }
    // Helper method to format fight initiation (in yellow background)
    private String startFight(String text) {
        return YELLOW_BACKGROUND + text + RESET + "\n\n"; // Fix: Removed ':' from the beginning
    }

    private String createCredits(String text) {
        return BLUE + text + RESET + "\n\n";
    }

    // Method to print the story with a typing effect
    private void printWithTypingEffect(String text) throws InterruptedException {
        String[] sentences = text.split("\n\n"); // Split by paragraph
        for (String sentence : sentences) {
            String[] words = sentence.split(" ");
            for (String word : words) {
                System.out.print(word + " ");
                Thread.sleep(TYPING_DELAY); // Simulate typing effect
            }
            System.out.println(); // Move to the next line after printing each sentence
            waitForEnter(); // Wait for user to press Enter
        }
    }

    // Method to wait for user to press Enter
    private void waitForEnter() {
        // Check if there's a next line available
        if (scanner.hasNextLine()) {
            scanner.nextLine(); // Wait for Enter key
        }
    }
}
