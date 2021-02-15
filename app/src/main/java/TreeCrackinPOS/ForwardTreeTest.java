package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

public class ForwardTreeTest {
    public static void main(String[] args) {
        final int TREE_CALL_RANGE = 220;
        final int CHUNK_A_X = 7;
        final int CHUNK_A_Z = 13;

        long[] treeSeedsIn = {
                264060651946591L,
                264109701204872L,
                264289093498668L,
                264593222850119L,
                264596903851107L,
                264694790839702L,
                264969973758492L,
                265185991149229L,
                265209371399324L,
                265591748144718L,
                265685880880109L,
                265761818518836L,
                265905218950694L,
                266076457961807L,
                266252864711741L,
                266380872885609L,
                266437110405067L,
                266497861911604L,
                266498824980207L,
                266629968349929L,
                266969212572034L,
                267048615028877L,
                267149633216483L,
                267358476288451L,
                267543812588147L,
                267907779259642L,
                267930357892316L,
                268075412619741L,
                268084510854546L,
                268164258907889L,
                268409366989781L,
                268436897848147L,
                268472885825359L,
                268490474667671L,
                268538708711945L,
                268656683601844L,
                268978568247470L,
                269019557735437L,
                269753080234300L,
                269761785083465L,
                270058612299580L,
                270105017011231L,
                270406767596530L,
                270409685258336L,
                270694991592013L,
                270745670352615L,
                270932546449271L,
                270934055692006L,
                271061105987819L,
                271189804450577L,
                271388341580975L,
                271714749345587L,
                271935038731256L,
                271952025193646L,
                272041012013373L,
                272488678554727L,
                272871430054734L,
                273054576724535L,
                273138325979448L,
                273213745095396L,
                273256807805009L,
                273913499845136L,
                274007633708013L,
                274048091254492L,
                274107191022026L,
                274109738326891L,
                274222816203055L,
                274711034184116L,
                275044914643463L,
                275071376886171L,
                275111500055022L,
                275772052865562L,
                275790026498501L,
                275834155602762L,
                275950519252734L,
                276026341242173L,
                276190625209916L,
                276195902090643L,
                276205997944300L,
                276231056144357L,
                276254877189347L,
                276451949192756L,
                276452891308901L,
                276602669832629L,
                276616916216368L,
                276706548392915L,
                276984004207868L,
                277011668039773L,
                277149186520858L,
                277180457499742L,
                277568446117667L,
                277576446065762L,
                277781523554026L,
                277791423964837L,
                277815106432097L,
                277881927450470L,
                278276352766755L,
                278309444046669L,
                278311815768318L,
                278312801476240L,
                278680568683036L,
                278726125952818L,
                278739538546096L,
                278931873011293L,
                278991776551140L,
                279328656385383L,
                279346027828108L,
                279422862138415L,
                279982840436202L,
                280025683669820L,
                280129090580469L,
                280196636180818L,
                280204415993609L,
                280294377346166L,
                280557566338159L,
                280595567603108L,
                280643744536866L,
                280671662529286L,
                280987933238260L,
                281208865111890L,
                281363853694274L,
        };

        int matches = 0;

        outer:
        for (long treeSeed : treeSeedsIn) {
            long initialSeed = treeSeed;
            boolean[] treesFound = new boolean[10];
            long[] treeSeeds = new long[10];
            treeSeed = LCG.JAVA.combine(-TREE_CALL_RANGE * 8).nextSeed(treeSeed);
            for (int callOffset = 0; callOffset < TREE_CALL_RANGE * 8; callOffset++) {
                treeSeed = LCG.JAVA.nextSeed(treeSeed);
                if (!treesFound[0]) { treesFound[0] = checkTree0(treeSeed, treeSeeds); }
                if (!treesFound[1]) { treesFound[1] = checkTree1(treeSeed, treeSeeds); }
                if (!treesFound[2]) { treesFound[2] = checkTree2(treeSeed, treeSeeds); }
                if (!treesFound[3]) { treesFound[3] = checkTree3(treeSeed, treeSeeds); }

                if (treesFound[0] && treesFound[1]) {
                    System.out.printf("seed %15d matches, tree seed: %15d, calloffset: %4d\n", initialSeed, treeSeed, callOffset);
                    matches++;
                    continue outer;
                }
            }
        }
        System.out.println("matches: " + matches);
    }

    public static void printBoolArray(boolean[] a) {
        for (boolean b : a) {
            System.out.print(b ? '1' : '0');
        }
        System.out.println();
    }

    public static boolean checkTree0(long seed, long[] treeSeeds) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  5) return false;
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  8) return false;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  1) return false;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  0) return false;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  1) return false;
        if ((((seed * 120681609298497L +  14307911880080L) >> 47) &  1) !=  0) return false;
        if ((((seed * 262331189124013L + 215905707320923L) >> 47) &  1) !=  1) return false;
        if ((((seed * 233752471717045L +  11718085204285L) >> 17) %  5) ==  0) return false;
        if ((((seed *  55986898099985L +  49720483695876L) >> 17) % 10) ==  0) return false;
        if ((((seed * 120950523281469L + 102626409374399L) >> 17) %  3) !=  1) return false;
        treeSeeds[0] = seed;

        return true;
    }

    public static boolean checkTree1(long seed, long[] treeSeeds) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  5) return false;
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  8) return false;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  1) return false;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  0) return false;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  1) return false;
        if ((((seed * 120681609298497L +  14307911880080L) >> 47) &  1) !=  0) return false;
        if ((((seed * 262331189124013L + 215905707320923L) >> 47) &  1) !=  1) return false;
        if ((((seed * 233752471717045L +  11718085204285L) >> 17) %  5) ==  0) return false;
        if ((((seed *  55986898099985L +  49720483695876L) >> 17) % 10) ==  0) return false;
        if ((((seed * 120950523281469L + 102626409374399L) >> 17) %  3) !=  1) return false;
        treeSeeds[1] = seed;

        return true;
    }

    public static boolean checkTree2(long seed, long[] treeSeeds) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  0) return false;
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 11) return false;
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  1) return false;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  0) return false;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  0) return false;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  1) return false;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  0) return false;
        if ((((seed * 120681609298497L +  14307911880080L) >> 47) &  1) !=  1) return false;
        if ((((seed * 262331189124013L + 215905707320923L) >> 47) &  1) !=  1) return false;
        if ((((seed * 233752471717045L +  11718085204285L) >> 17) %  5) ==  0) return false;
        if ((((seed *  55986898099985L +  49720483695876L) >> 17) % 10) ==  0) return false;
        if ((((seed * 120950523281469L + 102626409374399L) >> 17) %  3) !=  0) return false;
        treeSeeds[2] = seed;

        return true;
    }

    public static boolean checkTree3(long seed, long[] treeSeeds) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 15) return false;
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 15) return false;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  0) return false;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  0) return false;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  0) return false;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  0) return false;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  0) return false;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  0) return false;
        if ((((seed * 120681609298497L +  14307911880080L) >> 47) &  1) !=  0) return false;
        if ((((seed * 262331189124013L + 215905707320923L) >> 47) &  1) !=  0) return false;
        if ((((seed * 233752471717045L +  11718085204285L) >> 17) %  5) ==  0) return false;
        if ((((seed *  55986898099985L +  49720483695876L) >> 17) % 10) ==  0) return false;
        if ((((seed * 120950523281469L + 102626409374399L) >> 17) %  3) !=  2) return false;
        treeSeeds[3] = seed;

        return true;
    }

    public static int checkTree3e(long seed, long[] treeSeeds) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 15) return -1;
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 15) return -2;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  0) return -3;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  0) return -4;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  0) return -5;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  0) return -6;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  0) return -7;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  0) return -8;
        if ((((seed * 120681609298497L +  14307911880080L) >> 47) &  1) !=  0) return -9;
        if ((((seed * 262331189124013L + 215905707320923L) >> 47) &  1) !=  0) return -10;
        if ((((seed * 233752471717045L +  11718085204285L) >> 17) %  5) ==  0) return -11;
        if ((((seed *  55986898099985L +  49720483695876L) >> 17) % 10) ==  0) return -12;
        if ((((seed * 120950523281469L + 102626409374399L) >> 17) %  3) !=  2) return -13;
        treeSeeds[3] = seed;

        return 1;
    }
}
