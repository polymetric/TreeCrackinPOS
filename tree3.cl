if ((((seed * 233752471717045LU +  11718085204285LU) >> 47) &  1) !=  1) return 0; // leaf height
if (((((seed *     25214903917LU +              11LU) & 281474976710655LU) >> 17) %  3) !=  0) return 0; // type
if (((((seed * 205749139540585LU +    277363943098LU) & 281474976710655LU) >> 17) %  5) !=  3) return 0; // height
if (((((seed *  55986898099985LU +  49720483695876LU) & 281474976710655LU) >> 17) %  5) !=  0) return 0; // radius == 1
