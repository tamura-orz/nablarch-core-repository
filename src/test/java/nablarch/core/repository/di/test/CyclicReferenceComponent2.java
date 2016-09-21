package nablarch.core.repository.di.test;

public class CyclicReferenceComponent2 {
	private CyclicReferenceComponent3 component;
	public void setComponent(CyclicReferenceComponent3 component) {
		this.component = component;
	}

}
