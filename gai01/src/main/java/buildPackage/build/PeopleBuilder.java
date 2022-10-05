package buildPackage.build;

import buildPackage.People;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2022/10/5 10:25
 */
public class PeopleBuilder {
    private People people = new People();

    // 返回类型是PeopleBuildr，是为了能写链式结构.buildName().buildId();
    public PeopleBuilder id(int id) {
        this.people.setId(id);
        return this;
    }

    public PeopleBuilder name(String name) {
        this.people.setName(name);
        return this;
    }

    public People build() {
        return this.people;
    }
}
