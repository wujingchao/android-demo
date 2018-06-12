ADD_DEFINITIONS(-DHAVE_PTHREAD)

#from master branch...

set(libprotobuf_lite_files
  src/cpp/google/protobuf/arena.cc
  src/cpp/google/protobuf/arenastring.cc
  src/cpp/google/protobuf/extension_set.cc
  src/cpp/google/protobuf/generated_message_table_driven_lite.cc
  src/cpp/google/protobuf/generated_message_util.cc
  src/cpp/google/protobuf/implicit_weak_message.cc
  src/cpp/google/protobuf/io/coded_stream.cc
  src/cpp/google/protobuf/io/zero_copy_stream.cc
  src/cpp/google/protobuf/io/zero_copy_stream_impl_lite.cc
  src/cpp/google/protobuf/message_lite.cc
  src/cpp/google/protobuf/repeated_field.cc
  src/cpp/google/protobuf/stubs/bytestream.cc
  src/cpp/google/protobuf/stubs/common.cc
  src/cpp/google/protobuf/stubs/int128.cc
  src/cpp/google/protobuf/stubs/io_win32.cc
  src/cpp/google/protobuf/stubs/status.cc
  src/cpp/google/protobuf/stubs/statusor.cc
  src/cpp/google/protobuf/stubs/stringpiece.cc
  src/cpp/google/protobuf/stubs/stringprintf.cc
  src/cpp/google/protobuf/stubs/structurally_valid.cc
  src/cpp/google/protobuf/stubs/strutil.cc
  src/cpp/google/protobuf/stubs/time.cc
  src/cpp/google/protobuf/wire_format_lite.cc
)

set(libprotobuf_lite_includes
  src/cpp/google/protobuf/arena.h
  src/cpp/google/protobuf/arenastring.h
  src/cpp/google/protobuf/extension_set.h
  src/cpp/google/protobuf/generated_message_util.h
  src/cpp/google/protobuf/implicit_weak_message.h
  src/cpp/google/protobuf/io/coded_stream.h
  src/cpp/google/protobuf/io/zero_copy_stream.h
  src/cpp/google/protobuf/io/zero_copy_stream_impl_lite.h
  src/cpp/google/protobuf/message_lite.h
  src/cpp/google/protobuf/repeated_field.h
  src/cpp/google/protobuf/stubs/bytestream.h
  src/cpp/google/protobuf/stubs/common.h
  src/cpp/google/protobuf/stubs/int128.h
  src/cpp/google/protobuf/stubs/once.h
  src/cpp/google/protobuf/stubs/status.h
  src/cpp/google/protobuf/stubs/statusor.h
  src/cpp/google/protobuf/stubs/stringpiece.h
  src/cpp/google/protobuf/stubs/stringprintf.h
  src/cpp/google/protobuf/stubs/strutil.h
  src/cpp/google/protobuf/stubs/time.h
  src/cpp/google/protobuf/wire_format_lite.h
)


add_library(libprotobuf-lite
            STATIC
  ${libprotobuf_lite_files}
  ${libprotobuf_lite_includes}
)

target_link_libraries(libprotobuf-lite ${CMAKE_THREAD_LIBS_INIT})
target_include_directories(libprotobuf-lite PUBLIC src/cpp/)