import decorator.drinks.Beer;

public class Main {
    public static void main(String[] args) {
        Man man = new Man("cddd");
       // setValue(man);
        System.out.println(man.getName());
    }

    public static void setValue(Man str) {
        //todo 此时引用的str是另一个副本，和外层的man 是两个引用这里给它复制，只是把这个副本指向的地址修改了，外层的不受影响，如果没有修改地址，只是修改了对象属性的话，均受影响
        str.setName("ddddcc");
//      str = new Man("test");
    }

    public static class Man {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Man(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Man{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
