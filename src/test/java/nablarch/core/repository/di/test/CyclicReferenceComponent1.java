package nablarch.core.repository.di.test;

public class CyclicReferenceComponent1 {

	private CyclicReferenceComponent2 component;
	public void setComponent(CyclicReferenceComponent2 component) {
		this.component = component;
	}
}
