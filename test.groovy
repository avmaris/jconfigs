def getRepoHosts () {
	return [
		aws: [baseUrl: 'https://git-codecommit.us-east-1.amazonaws.com/v1/repos/<<repoName>>'],
		github: [baseUrl: 'https://github.com/systemviewinc/<<repoName>>.git']
	]
}
def getSubmodules() {
	return [
		vsi: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		vsi_runtime: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		vsi_tcl: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		vsi_interfaces: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		vsi_ilang: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		fpga_driver: [repoHost: 'github', jobs: ['build/driver_vsi', /integration\/.*(hw|device|data_transfer).*/]],
		'vsi-examples':  [repoHost: 'github', jobs: ['build/vsi', /integration\/.+/]],
		vsi_scripts: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		vsi_license: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.*license.*/]],
		vsi_support_files: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		vsi_clang: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		rpmsg_driver: [repoHost: 'aws', jobs: ['build/driver_rpmsg', /integration\/R5/]],
		freertos_ext: [repoHost: 'aws', jobs: ['build/vsi', /integration\/R5/]],
		rpc_simulator: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.*simulation.*/]],
		templ_utils: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		vsi_shmem: [repoHost: 'aws', jobs: ['build/vsi']],
		vsi_javaparser: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.*java.*/]],
		vsi_pythonparser: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.*python.*/]],
		docs: [repoHost: 'github', jobs: ['build/vsi']],
		vsi_launcher: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.*license.*/]],
		vsi_utils: [repoHost: 'github', jobs: ['build/vsi', /integration\/.+/]],
		vsi_trace: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		vsi_example_designs: [repoHost: 'aws', jobs: ['build/vsi', /integration\/.+/]],
		freertos901_xilinx_bsp_armr5: [repoHost: 'aws', jobs: ['build/vsi', /integration\/R5/]],
	]
}

class Scm {
	class RemoteConfig {
		String url;
		RemoteConfig(_url) {
			url = _url
		}
		def getUrl() {
			return url;
		}
	}
	RemoteConfig []remoteConfigs
	Scm () {
		def _remoteConfigs = []
		_remoteConfigs << new RemoteConfig('https://git-codecommit.us-east-1.amazonaws.com/v1/repos/rpmsg_driver')
		_remoteConfigs << new RemoteConfig('https://git-codecommit.us-east-1.amazonaws.com/v1/repos/fpga_driver')
		_remoteConfigs << new RemoteConfig('https://git-codecommit.us-east-1.amazonaws.com/v1/repos/vsi_jenkins_config')
		remoteConfigs = _remoteConfigs
	}
	def getUserRemoteConfigs() {
		return remoteConfigs;
	}
}

class Utils {
	static def getAllItems() {
		return [
			[fullName: 'build/vsi'],
			[fullName: 'build/driver_vsi'],
			[fullName: 'build/driver_rpmsg'],
			[fullName: 'integration/blocking_read'],
			[fullName: 'integration/arm_sort'],
			[fullName: 'integration/simulation'],
			[fullName: 'integration/license_test'],
			[fullName: 'integration/R5'],
			[fullName: 'integration/python_device'],

		]
	}
}

def getListOfBuilds(scm) {
	def builds = []
	scm.getUserRemoteConfigs().each {
		def repoName = it.getUrl().split('/').last().replace(/.git/, '')
		def submodule = getSubmodules()[repoName]
		if (submodule != null) {
			submodule.jobs.each { j ->
				Utils.getAllItems().each {
					if (it.fullName =~ j) {
						println "${repoName} ${j}: ${it.fullName}"
						builds << "${it.fullName}"
					}
				}
			}
		} else {
			println "skipping ${repoName}"
		}
	}
	uniqueBuilds = builds.toUnique().toSorted()
	uniqueBuilds.each {
		println it
	}
}

def getSubmodulesUrl () {
	return getSubmodules().collect { key, value ->
		[
			name: key,
			url: getRepoHosts()[value.repoHost]?.baseUrl.replace('<<repoName>>', key)
		]
	}
}

getSubmodulesUrl().each { mod ->
	println "${mod.name} ${mod.url}"
}
def scm = new Scm();
getListOfBuilds(scm)

def build_fail = []

if (build_fail.size() > 0) {
	println build_fail
}

build_fail << "fun"

if (build_fail.size() > 0) {
	println "more ${build_fail}"
}
