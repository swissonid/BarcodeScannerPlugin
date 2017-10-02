#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'barcodescanner'
  s.version          = '0.0.1'
  s.summary          = 'A new flutter plugin project.'
  s.description      = <<-DESC
A very simple flutter plugin to scan barcodes and qr codes
                       DESC
  s.homepage         = 'https://github.com/swissonid/BarcodeScannerPlugin'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Benjamin Sauer' => 'ben@emvolution.me' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.dependency 'ZXingObjC'

  s.resource_bundles = {
      'flutter_barcodescanner' => ['Assets/**/*.xib', 'Assets/**/*.png']
  }

  s.ios.deployment_target = '8.0'
end
