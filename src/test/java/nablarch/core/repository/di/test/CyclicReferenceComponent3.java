package nablarch.core.repository.di.test;

public class CyclicReferenceComponent3 {
	private CyclicReferenceComponent1 component;
	public void setComponent(CyclicReferenceComponent1 component) {
		this.component = component;
	}

}
